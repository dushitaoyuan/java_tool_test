package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.utils.HelpUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 大文件归并排序
 * <p>
 * 速度提升优化点:
 * 1. 分拆排序多线程
 * 2. 归并文件多线程
 * 3. 拆分归并 并行
 * <p>
 * 同样机器配置,1000万数据:
 * 多线程 8.7s
 * 单线程:19s
 * 1亿数据
 * 多线程 150秒
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
    /**
     * 文件是否分拆完毕
     */
    private AtomicBoolean splitDone = new AtomicBoolean(false);
    private Integer threadSize;

    private ThreadPoolExecutor mergePool = null;

    public BigFileMergeSort(Integer splitSize, String srcFile, String spiltDir, String resultDir, String mergeFile, Comparator<Integer> comparator
            , Integer threadSize) {
        this.splitSize = splitSize;
        this.srcFile = srcFile;
        this.comparator = comparator;
        this.spiltDir = spiltDir;
        this.mergeFile = mergeFile;
        this.resultDir = resultDir;
        this.chunkQueue = new LinkedBlockingDeque<>();
        this.threadSize = threadSize;
        mergePool = new ThreadPoolExecutor(threadSize, threadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
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
        fileSpilt();
        int limit = 4;
        for (int i = 0; i < threadSize; i++) {
            mergePool.execute(new IntFileMergeTask(limit, splitDone));
        }
        mergePool.shutdown();
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
        Long start = System.currentTimeMillis();
        fileSpilt();
        Long end = System.currentTimeMillis();
        System.out.println("拆分总耗时:" + (end - start));
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
                newChunk(chunkIntList, false);
                chunkIntList = new ArrayList<>(splitSize);
                spiltCount++;
                chunkCount = 0;
            }
        }
        if (chunkCount > 0) {
            newChunk(chunkIntList, true);
            spiltCount++;
        }
        src.close();
        HelpUtil.unMapedBuffer(map);
        System.out.println("总共切分片数:" + spiltCount);
        System.out.println("读取数字总数:" + allCount);


    }

    private void newChunk(List<Integer> chunkIntList, boolean isLast) throws Exception {
        if (!chunkIntList.isEmpty()) {
            if (mergePool != null) {
                if (mergePool.getQueue().size() > threadSize) {
                    doNewChunk(chunkIntList, isLast);
                } else {
                    mergePool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                doNewChunk(chunkIntList, isLast);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } else {
                doNewChunk(chunkIntList, isLast);
            }
        }

    }


    private void doNewChunk(List<Integer> chunkIntList, boolean isLast) throws Exception {
        File chunkFile = new File(spiltDir, HelpUtil.getRandomChunk());
        RandomAccessFile chunk = new RandomAccessFile(chunkFile, "rw");
        MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkIntList.size() * 4);
        //排序后写入
        chunkIntList.stream().sorted(comparator).forEach(num -> {
            chunkByteBuffer.putInt(num);
        });
        chunk.close();
        HelpUtil.unMapedBuffer(chunkByteBuffer);
        chunkQueue.add(chunkFile);
        if (isLast) {
            splitDone.set(true);
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
                throw new RuntimeException("文件归并失败", e);
            }
        }
    }

}
