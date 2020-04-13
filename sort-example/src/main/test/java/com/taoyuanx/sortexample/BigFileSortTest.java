package com.taoyuanx.sortexample;

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
    private int numNum;

    @Before
    public void before() throws IOException {
        this.bigFile = "d://file/bigNum.txt";
        this.splitDir = "d://file/chunk/";
        FileUtils.forceMkdir(new File(splitDir));
        this.numNum = 100000000;
    }


    @Test
    public void createBigNumTxt() throws Exception {
        int numRange = 100;
        FileChannel dest = (FileChannel) Channels.newChannel(new FileOutputStream(bigFile));
        Random random = new Random();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 << 20);
        int count = 0, maxNum = (4 << 20) / 10;
        for (int i = 0; i < this.numNum; i++) {
            if (count > maxNum) {
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
        RandomAccessFile src = new RandomAccessFile(bigFile, "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int count = 0;
        while (map.hasRemaining()) {
            map.getInt();
            map.getChar();
            count++;
        }
        System.out.println("总数是否匹配:" + Objects.equals(count, numNum));
    }

    @Test
    public void bigFileNumTxtSpiltTest() throws Exception {
        RandomAccessFile src = new RandomAccessFile(bigFile, "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int spiltMaxNum = 100000, spiltMinNum = 50000, chunkCount = 0, allCount = 0, spiltCount = 0;
        Random random = new Random();
        int spiltNum = random.nextInt(spiltMaxNum - spiltMinNum) + spiltMinNum;
        RandomAccessFile chunk = new RandomAccessFile(new File(splitDir, getRandomChunk()), "rw");
        MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, spiltNum * 4);
        List<Integer> intList = new ArrayList<>(spiltNum);
        spiltCount++;
        while (map.hasRemaining()) {
            int num = map.getInt();
            map.getChar();
            allCount++;
            chunkCount++;
            if (chunkCount > spiltNum || chunkByteBuffer.remaining() < 4) {
                chunk.close();
                chunk = new RandomAccessFile(new File(splitDir, getRandomChunk()), "rw");
                chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, spiltNum * 4);
                chunkCount = 0;
                spiltCount++;
            } else {
                chunkByteBuffer.putInt(num);
            }
        }
        if (chunkCount > 0) {
            chunk.close();
        }
        System.out.println("总共切分片数:" + spiltCount);
        System.out.println("读取数字总数:" + allCount);

    }

    private String getRandomChunk() {
        String chunkFilePrefix = "chunk_num_";
        return chunkFilePrefix + UUID.randomUUID().toString().replaceAll("-", "") + ".chunk";
    }
}
