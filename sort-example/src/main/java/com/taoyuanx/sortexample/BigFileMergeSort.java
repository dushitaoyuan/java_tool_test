package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;
import com.taoyuanx.sortexample.utils.RandomNameUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 大文件归并排序
 */
public class BigFileMergeSort {


    /*@Override
    public T[] sort(T[] array, Comparator comparator) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length >>> 1;
        T[] leftArray = sort(Arrays.copyOfRange(array, 0, mid), comparator);
        T[] rightArray = sort(Arrays.copyOfRange(array, mid, array.length), comparator);
        return merge(leftArray, rightArray, comparator);
    }*/


    private Integer splitSize;
    private String srcFile;
    private String spiltDir;
    private Comparator<Integer> comparator;
    private String mergeFile;

    public BigFileMergeSort(Integer splitSize, String srcFile, String spiltDir, String mergeFile, Comparator<Integer> comparator) {
        this.splitSize = splitSize;
        this.srcFile = srcFile;
        this.comparator = comparator;
        this.spiltDir = spiltDir;
        this.mergeFile = mergeFile;
    }

    private void fileSpilt() throws Exception {
        RandomAccessFile src = new RandomAccessFile(srcFile, "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int spiltMaxNum = 100000, spiltMinNum = 50000, chunkCount = 0, allCount = 0, spiltCount = 0;
        Random random = new Random();
        int spiltNum = random.nextInt(spiltMaxNum - spiltMinNum) + spiltMinNum;
        List<Integer> chunkIntList = new ArrayList<>(spiltNum);
        while (map.hasRemaining()) {
            int num = map.getInt();
            map.getChar();
            allCount++;
            chunkCount++;
            chunkIntList.add(num);
            if (chunkIntList.size() >= spiltNum) {
                newChunk(chunkIntList);
                chunkIntList = new ArrayList<>(spiltNum);
                spiltCount++;
                chunkCount = 0;
            }
        }
        if (chunkCount > 0) {
            newChunk(chunkIntList);
        }
        System.out.println("总共切分片数:" + spiltCount);
        System.out.println("读取数字总数:" + allCount);
    }

    private void newChunk(List<Integer> chunkIntList) throws Exception {
        if (!chunkIntList.isEmpty()) {
            RandomAccessFile chunk = new RandomAccessFile(new File(this.spiltDir, RandomNameUtil.getRandomChunk()), "rw");
            MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkIntList.size() * 4);
            //排序后写入
            chunkIntList.stream().sorted(comparator).forEach(num -> {
                chunkByteBuffer.putInt(num);
            });
            chunk.close();
        }
    }

    public void sort() throws Exception {
        Collection<File> chunkFileList = FileUtils.listFiles(new File(spiltDir), new SuffixFileFilter(".chunk"), null);
        Iterator<File> iterator = chunkFileList.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        File file = iterator.next();

        fileMerge(IntFileStream.EMPTY, new IntFileStream(file), mergeFile, comparator);
        while (iterator.hasNext()) {
            file = iterator.next();
            fileMerge(new IntFileStream(mergeFile), new IntFileStream(file), mergeFile, comparator);
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
                byteBuffer.putInt(right.next());
            } else if (leftHasNext && !rightHasNext) {
                byteBuffer.putInt(left.next());
            } else if (leftHasNext && rightHasNext) {
                if (comparator.compare(leftNum, rightNum) > 0) {
                    byteBuffer.putInt(rightNum);
                } else {
                    byteBuffer.putInt(leftNum);

                }
            }
        }

    }

    private static class IntFileStream implements Iterator<Integer> ,Cloneable{
        private MappedByteBuffer chunkBuffer;
        private RandomAccessFile chunkFile;
        public static final IntFileStream EMPTY = new IntFileStream();

        private IntFileStream() {
        }


        public IntFileStream(String chunkSrc) {
            try {
                chunkFile = new RandomAccessFile(chunkSrc, "rw");
                chunkBuffer = chunkFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkFile.length());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public IntFileStream(File chunkSrc) {
            try {
                chunkFile = new RandomAccessFile(chunkSrc, "rw");
                chunkBuffer = chunkFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkFile.length());
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
    }

}
