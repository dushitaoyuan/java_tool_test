package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.utils.HelpUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 大文件归并排序
 * <p>
 * 速度提升优化点:
 * 1. 分拆排序多线程
 * 2. 归并文件多线程
 * <p>
 * 同样机器配置,1000万数据:
 * 多线程 8.7s
 * 单线程:19s
 * 1亿数据
 * 多线程 15秒
 * 单线程 太慢了,测不了
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
    private Integer mergeThreadSize;

    public BigFileMergeSort(Integer splitSize, String srcFile, String spiltDir, String resultDir, String mergeFile, Comparator<Integer> comparator
            , Integer mergeThreadSize) {
        this.splitSize = splitSize;
        this.srcFile = srcFile;
        this.comparator = comparator;
        this.spiltDir = spiltDir;
        this.mergeFile = mergeFile;
        this.resultDir = resultDir;
        this.chunkQueue = new LinkedBlockingDeque<>();
        /**
         * 文件是否分拆完毕
         */
        this.splitDone = new AtomicBoolean(false);
        this.mergeThreadSize = mergeThreadSize;
    }

    public BigFileMergeSort(Integer splitSize, String srcFile, String spiltDir, String resultDir, String mergeFile, Comparator<Integer> comparator
    ) {
        this.splitSize = splitSize;
        this.srcFile = srcFile;
        this.comparator = comparator;
        this.spiltDir = spiltDir;
        this.mergeFile = mergeFile;
        this.resultDir = resultDir;
        this.chunkQueue = new LinkedBlockingDeque<>();
    }


    public void sortMultiThread() throws Exception {
        ThreadPoolExecutor mergePool = new ThreadPoolExecutor(mergeThreadSize, mergeThreadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        mergePool.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                fileSpilt();
                splitDone.set(true);
            }
        });
        int limit = 4;
        for (int i = 0; i < mergeThreadSize; i++) {
            mergePool.execute(new IntFileMergeTask(limit, splitDone));
        }
        mergePool.shutdown();
        while (mergePool.awaitTermination(3, TimeUnit.SECONDS)) {
            Thread.sleep(3000L);
        }
        File chunkFileLeft = chunkQueue.poll();
        File mergeFile = null;
        while (chunkQueue.size() > 0) {
            mergeFile = new File(resultDir, HelpUtil.getRandomResultChunk());
            File chunkFileRight = chunkQueue.poll();
            fileMerge(new IntFileStream(chunkFileLeft), new IntFileStream(chunkFileRight), mergeFile.getAbsolutePath(), comparator);
            chunkFileLeft = mergeFile;
        }
        RandomAccessFile src = new RandomAccessFile(chunkFileLeft, "r");
        RandomAccessFile dest = new RandomAccessFile(new File(this.mergeFile), "rw");
        src.getChannel().transferTo(0, src.length(), dest.getChannel());
        src.close();
        dest.close();
        FileUtils.deleteQuietly(chunkFileLeft);
        System.out.println("多线程结束");
    }

    public void sortSingleThread() throws Exception {
        fileSpilt();
        File chunkFileLeft = chunkQueue.poll();
        File mergeFile = null;
        while (chunkQueue.size() > 0) {
            mergeFile = new File(resultDir, HelpUtil.getRandomResultChunk());
            File chunkFileRight = chunkQueue.poll();
            fileMerge(new IntFileStream(chunkFileLeft), new IntFileStream(chunkFileRight), mergeFile.getAbsolutePath(), comparator);
            chunkFileLeft = mergeFile;
        }
        RandomAccessFile src = new RandomAccessFile(chunkFileLeft, "r");
        RandomAccessFile dest = new RandomAccessFile(new File(this.mergeFile), "rw");
        src.getChannel().transferTo(0, src.length(), dest.getChannel());
        src.close();
        dest.close();
        FileUtils.deleteQuietly(chunkFileLeft);
        System.out.println("单线程结束");

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
        src.close();
        HelpUtil.unMapedBuffer(map);
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
            chunk.close();
            HelpUtil.unMapedBuffer(chunkByteBuffer);
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
        private File chunkFile;
        private RandomAccessFile chunkRandomFile;

        public IntFileStream(String chunkSrc) {
            this(new File(chunkSrc));
        }

        public IntFileStream(File chunkSrc) {
            try {
                this.chunkFile = chunkSrc;
                this.chunkRandomFile = new RandomAccessFile(chunkFile, "rw");
                chunkBuffer = chunkRandomFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkFile.length());
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
            this.chunkRandomFile.close();
            HelpUtil.unMapedBuffer(chunkBuffer);
            FileUtils.deleteQuietly(chunkFile);
        }
    }

    private class IntFileMergeTask implements Runnable {
        private int limit;
        private AtomicBoolean done;

        public IntFileMergeTask(int limit, AtomicBoolean done) {
            this.limit = limit;
            this.done = done;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    File chunkFileLeft = null, chunkFileRight = null;
                    synchronized (chunkQueue) {
                        if (done.get() && chunkQueue.size() < limit) {
                            return;
                        }
                        if (!done.get() && chunkQueue.size() < limit) {
                            Thread.sleep(3000L);
                            continue;
                        }
                        chunkFileLeft = chunkQueue.poll();
                        chunkFileRight = chunkQueue.poll();
                    }
                    File mergeFile = new File(resultDir, HelpUtil.getRandomResultChunk());
                    fileMerge(new IntFileStream(chunkFileLeft), new IntFileStream(chunkFileRight), mergeFile.getAbsolutePath(), comparator);
                    chunkQueue.add(mergeFile);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
