package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.utils.HelpUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * @author dushitaoyuan
 * @desc 大文件外部排序测试
 * @date 2020/4/13
 */
public class BigFileSortTest {
    private String bigFile;
    private String splitDir;
    private String resultDir;
    private int numNum;
    private Comparator<Integer> ascComparator = (x, y) -> {
        return x - y;
    };
    private Comparator<Integer> descComparator = ascComparator.reversed();

    @Before
    public void before() throws IOException {
        this.bigFile = "d://file/bigNum.txt";
        this.splitDir = "d://file/chunk/";
        this.resultDir = "d://file/chunk/result";
        FileUtils.forceMkdir(new File(splitDir));
        FileUtils.forceMkdir(new File(resultDir));
        this.numNum = 10000000;
    }

    @Test
    public void sortTest() throws Exception {
        createBigNumTxt();
        Long start = System.currentTimeMillis();
        String mergeFile = "d://file/merge.txt";
        BigFileMergeSort bigFileMergeSort = new BigFileMergeSort(100000, bigFile, splitDir, resultDir, mergeFile, descComparator);
        bigFileMergeSort.sort();
        Long endSort = System.currentTimeMillis();
        BigFileMergeSort.IntFileStream intFileStream = new BigFileMergeSort.IntFileStream(mergeFile);
        int count = 0;
        while (intFileStream.hasNext()) {
            intFileStream.next();
            count++;
        }
        Long endRead = System.currentTimeMillis();
        System.out.println("排序耗时:" + (endSort - start));
        System.out.println("读取耗时:" + (endRead - endSort));
        System.out.println("总数是否匹配:" + Objects.equals(count, numNum));
        intFileStream.close();
    }

    @Test
    public void createBigNumTxt() throws Exception {
        int numRange = 100;
        FileChannel dest = (FileChannel) Channels.newChannel(new FileOutputStream(bigFile));
        Random random = new Random();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 << 20);
        int count = 0;
        for (int i = 0; i < this.numNum; i++) {
            if (byteBuffer.remaining() < 6) {
                byteBuffer.flip();
                dest.write(byteBuffer);
                byteBuffer.rewind();
                count = 0;
            }
            byteBuffer.putInt(random.nextInt(numRange));
            byteBuffer.putChar(',');
            count++;

        }
        if (count > 0) {
            byteBuffer.flip();
            dest.write(byteBuffer);
        }
        dest.close();
    }

    @Test
    public void bigFileReadTest() throws Exception {
        RandomAccessFile src = new RandomAccessFile("d:\\file\\chunk\\result\\chunk_num_resultf615a78f26b1474d8dfafe6cf5883e04.result", "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int count = 0;
        while (map.hasRemaining()) {
            int num = map.getInt();
            System.out.println(num);
            map.getChar();
            count++;
        }
        HelpUtil.cleanBuffer(map);
        System.out.println("总数是否匹配:" + Objects.equals(count, numNum));
    }

    @Test
    public void bigFileNumTxtSpiltTest() throws Exception {
        RandomAccessFile src = new RandomAccessFile(bigFile, "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int chunkCount = 0, allCount = 0, spiltCount = 0;
        int spiltNum = 300;
        List<Integer> chunkIntList = new ArrayList<>(spiltNum);
        spiltCount++;
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
        HelpUtil.cleanBuffer(map);

    }

    private void newChunk(List<Integer> chunkIntList) throws Exception {
        if (!chunkIntList.isEmpty()) {
            RandomAccessFile chunk = new RandomAccessFile(new File(splitDir, HelpUtil.getRandomChunk()), "rw");
            MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkIntList.size() * 4);
            //排序后写入
            chunkIntList.stream().sorted(descComparator).forEach(num -> {
                chunkByteBuffer.putInt(num);
            });
            chunk.close();
        }
    }

}
