package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.utils.HelpUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 大文件归并排序
 */
public class BigFileMergeSort {


    private Integer splitSize;
    private String srcFile;
    private String spiltDir;
    private Comparator<Integer> comparator;
    private String mergeFile;
    private String resultDir;
    private LinkedBlockingDeque<File> chunkQueue;
    private AtomicBoolean splitDone;

    public BigFileMergeSort(Integer splitSize, String srcFile, String spiltDir, String resultDir, String mergeFile, Comparator<Integer> comparator) {
        this.splitSize = splitSize;
        this.srcFile = srcFile;
        this.comparator = comparator;
        this.spiltDir = spiltDir;
        this.mergeFile = mergeFile;
        this.resultDir = resultDir;
        this.chunkQueue = new LinkedBlockingDeque<>();
        this.splitDone = new AtomicBoolean(false);
    }

    public void sort() throws Exception {
        ThreadPoolExecutor poolExecutor= (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        poolExecutor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                fileSpilt();
                splitDone.set(true);
            }
        });
        File chunkFileLeft = null, chunkFileRight = null;
        while (!splitDone.get()||chunkQueue.size() > 0) {
            if (!splitDone.get() && chunkQueue.size() < 2) {
                Thread.sleep(3000L);
                continue;
            } else if (splitDone.get() && chunkQueue.size() == 1) {
                File mergeFile = chunkQueue.poll();
                RandomAccessFile src = new RandomAccessFile(mergeFile, "r");
                RandomAccessFile dest = new RandomAccessFile(this.mergeFile, "rw");
                src.getChannel().transferTo(0, src.length(), dest.getChannel());
                dest.close();
                FileUtils.deleteQuietly(mergeFile);
            } else if (chunkQueue.size() >= 2) {
                chunkFileLeft = chunkQueue.poll();
                chunkFileRight = chunkQueue.poll();
                poolExecutor.execute(new IntFileMergeTask(chunkFileLeft, chunkFileRight));
            }
        }
        while (poolExecutor.awaitTermination(3,TimeUnit.SECONDS)){
            Thread.sleep(3000L);
        }

    }


    private void fileSpilt() throws Exception {
        RandomAccessFile src = new RandomAccessFile(srcFile, "r");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, src.length());
        int chunkCount = 0, allCount = 0, spiltCount = 0;
        List<Integer> chunkIntList = new ArrayList<>(splitSize);
        while (map.hasRemaining()) {
            int num = map.getInt();
            map.getChar();
            allCount++;
            chunkCount++;
            chunkIntList.add(num);
            if (chunkIntList.size() >= splitSize) {
                newChunk(chunkIntList);
                chunkIntList = new ArrayList<>(splitSize);
                spiltCount++;
                chunkCount = 0;
            }
        }
        if (chunkCount > 0) {
            newChunk(chunkIntList);
        }
        HelpUtil.cleanBuffer(map);
        System.out.println("总共切分片数:" + spiltCount);
        System.out.println("读取数字总数:" + allCount);
    }

    private void newChunk(List<Integer> chunkIntList) throws Exception {
        if (!chunkIntList.isEmpty()) {
            File chunkFile = new File(this.spiltDir, HelpUtil.getRandomChunk());
            RandomAccessFile chunk = new RandomAccessFile(chunkFile, "rw");
            MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkIntList.size() * 4);
            //排序后写入
            chunkIntList.stream().sorted(comparator).forEach(num -> {
                chunkByteBuffer.putInt(num);
            });
            HelpUtil.cleanBuffer(chunkByteBuffer);
            chunk.close();
            chunkQueue.add(chunkFile);
        }
    }


    private void fileMerge(IntFileStream left, IntFileStream right, String mergeFile, Comparator comparator) throws Exception {
        RandomAccessFile merge = new RandomAccessFile(mergeFile, "rw");
        FileChannel mergeChannel = merge.getChannel();
        boolean leftHasNext = false, rightHasNext = false;
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 << 20);
        while ((leftHasNext = left.hasNext()) || (rightHasNext = right.hasNext())) {
            if (byteBuffer.remaining() < 4) {
                byteBuffer.flip();
                mergeChannel.write(byteBuffer);
                byteBuffer.rewind();
            }

            Integer leftNum = null, rightNum = null;
            if (leftHasNext) {
                leftNum = left.next();
            }
            if (rightHasNext) {
                rightNum = right.next();
            }
            if (!leftHasNext && rightHasNext) {
                byteBuffer.putInt(rightNum);
            } else if (leftHasNext && !rightHasNext) {
                byteBuffer.putInt(leftNum);
            } else if (leftHasNext && rightHasNext) {
                if (comparator.compare(leftNum, rightNum) > 0) {
                    byteBuffer.putInt(rightNum);
                } else {
                    byteBuffer.putInt(leftNum);
                }
            }
        }
        if (byteBuffer.position() > 0) {
            byteBuffer.flip();
            mergeChannel.write(byteBuffer);
        }
        mergeChannel.close();
        merge.close();
        left.close();
        right.close();

    }

    public static class IntFileStream implements Iterator<Integer>, Closeable {
        private MappedByteBuffer chunkBuffer;
        private RandomAccessFile chunkFile;
        public static final IntFileStream EMPTY = new IntFileStream();

        private IntFileStream() {
        }


        public IntFileStream(String chunkSrc) {
            try {
                chunkFile = new RandomAccessFile(chunkSrc, "r");
                chunkBuffer = chunkFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, chunkFile.length());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public IntFileStream(File chunkSrc) {
            try {
                chunkFile = new RandomAccessFile(chunkSrc, "r");
                chunkBuffer = chunkFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, chunkFile.length());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            if (chunkBuffer == null) {
                return false;
            }
            return this.chunkBuffer.hasRemaining();
        }

        @Override
        public Integer next() {
            return this.chunkBuffer.getInt();
        }

        @Override
        public void close() throws IOException {
            HelpUtil.cleanBuffer(chunkBuffer);
        }
    }

    private class IntFileMergeTask implements Runnable {
        private File chunkFileLeft;
        private File chunkFileRight;

        public IntFileMergeTask(File chunkFileLeft, File chunkFileRight) {
            this.chunkFileLeft = chunkFileLeft;
            this.chunkFileRight = chunkFileRight;
        }

        @SneakyThrows
        @Override
        public void run() {
            File mergeFile = new File(resultDir, HelpUtil.getRandomResultChunk());
            fileMerge(new IntFileStream(chunkFileLeft), new IntFileStream(chunkFileRight), mergeFile.getAbsolutePath(), comparator);
            chunkQueue.add(mergeFile);
            FileUtils.deleteQuietly(chunkFileLeft);
            FileUtils.deleteQuietly(chunkFileRight);
        }
    }

}
