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
    private Comparator<Integer> ascComparator = Comparator.comparingInt(x -> x);
    private Comparator<Integer> descComparator = ascComparator.reversed();

    @Before
    public void before() throws IOException {

        this.bigFile = "/Users/taoyuan/tmp/file/bigNum.txt";
        this.splitDir = "/Users/taoyuan/tmp/file/chunk/";
        this.resultDir = "/Users/taoyuan/tmp/file/chunk/result";
        FileUtils.forceMkdir(new File(splitDir));
        FileUtils.forceMkdir(new File(resultDir));
        this.numNum = 100000000;
    }

    @Test
    public void sortTest() throws Exception {
        createBigNumTxt();
        Long start = System.currentTimeMillis();
        String mergeFile = "/Users/taoyuan/tmp/file/merge.txt";
        BigFileMergeSort bigFileMergeSort = new BigFileMergeSort(100000, bigFile, splitDir, resultDir, mergeFile, descComparator, 4);
        bigFileMergeSort.sortMultiThread();
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
    }

    @Test
    public void topTest() throws Exception {
        String mergeFile = "/Users/taoyuan/tmp/file/merge.txt";

        BigFileMergeSort.IntFileStream intFileStream = new BigFileMergeSort.IntFileStream(mergeFile);
        ArrayList<Integer> topList = new ArrayList<>();
        int topNum = 5;
        Map<Integer, Integer> numCount = new HashMap<>();

        Comparator<Integer> descComparator = (x, y) -> {
            return numCount.get(y) - numCount.get(x);
        };
        while (intFileStream.hasNext()) {
            Integer next = intFileStream.next();
            Integer count = numCount.get(next);
            if (count == null) {
                // 当不存在于map中时，认为已经map中的数字出现次数已经统计完毕
                if (topList.size() > topNum) {
                    Collections.sort(topList, descComparator);

                    Integer minNum = topList.remove(topList.size() - 1);
                    numCount.remove(minNum);
                }
                numCount.put(next, 1);
                topList.add(next);
            } else {
                numCount.put(next, numCount.get(next) + 1);
            }
        }
        int count = 0;
        Collections.sort(topList, descComparator);
        System.out.println(numCount);
        System.out.println(topList);
        System.out.println(count);

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
    public void readResultBigNumTxt() throws Exception {
        String mergeFile = "/Users/taoyuan/tmp/file/merge.txt";

        BigFileMergeSort.IntFileStream intFileStream = new BigFileMergeSort.IntFileStream(mergeFile);

        int count = 0;
        while (intFileStream.hasNext()) {
            Integer next = intFileStream.next();
            count++;
        }
        System.out.println("总数：" + count);
    }

    @Test
    public void bigFileReadTest() throws Exception {

        RandomAccessFile src = new RandomAccessFile(bigFile, "rw");
        MappedByteBuffer map = src.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
        int count = 0;
        while (map.hasRemaining()) {
            int num = map.getInt();
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
        int spiltNum = 10000;
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
        HelpUtil.unMapedBuffer(map);

    }

    private void newChunk(List<Integer> chunkIntList) throws Exception {
        if (!chunkIntList.isEmpty()) {
            File file = new File(splitDir, HelpUtil.getRandomChunk());
            RandomAccessFile chunk = new RandomAccessFile(file, "rw");
            MappedByteBuffer chunkByteBuffer = chunk.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, chunkIntList.size() * 4);
            //排序后写入
            chunkIntList.stream().sorted(descComparator).forEach(num -> {
                chunkByteBuffer.putInt(num);
            });
            chunk.close();
            HelpUtil.unMapedBuffer(chunkByteBuffer);
            FileUtils.deleteQuietly(file);
        }
    }

}
