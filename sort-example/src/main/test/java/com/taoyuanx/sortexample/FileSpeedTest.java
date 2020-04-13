package com.taoyuanx.sortexample;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author dushitaoyuan
 * @desc 文件读取速度测试
 * @date 2019/7/8
 */
public class FileSpeedTest {

    /**
     * 测试结论 FileChannel 较快,理论上 MappedByteBuffer 较快,不知道为啥我测出相反的结论
     * 另外也对比了MappedByteBuffer的使用坑点(关闭bug,超大文件),所以决定使用filechannel
     */

    @Test
    public void fileChannelTest() throws Exception {
        Long start = System.currentTimeMillis();
        RandomAccessFile src = new RandomAccessFile(new File("d://file/500.exe"), "r");
        FileChannel in = src.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4 * 1024 * 1024);
        FileChannel out = (FileChannel) Channels.newChannel(new FileOutputStream("d://file/temp_channel_500.exe"));
    /*    int len = 0;
        while ((len = in.read(buffer)) > 0) {
            buffer.flip();
            in.transferTo(0,in.size(),out);
            out.write(buffer.array(), 0, len);
            buffer.clear();
        }*/
        in.transferTo(0, in.size(), out);
        Long end = System.currentTimeMillis();
        System.out.println("FileChannel耗时:" + (end - start));
    }

    @Test
    public void mappedByteBufferTest() throws Exception {
        Long start = System.currentTimeMillis();
        RandomAccessFile src = new RandomAccessFile(new File("d://file/500.exe"), "r");
        MappedByteBuffer srcMap = src.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, src.length());
        RandomAccessFile dest = new RandomAccessFile("d://file/temp_mapped_500.exe", "rw");
        MappedByteBuffer desMap = dest.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, src.length());
      /*  byte[] buf = null;
        while ((buf = mapRead(srcMap, 4 * 1024 * 1024)) != null) {
            desMap.put(buf);
        }
        Long end = System.currentTimeMillis();
        src.close();
        dest.close();*/
        desMap.put(srcMap);
        src.close();
        dest.close();
        Long end = System.currentTimeMillis();
        System.out.println("MappedByteBuffer耗时:" + (end - start));
    }

    public byte[] mapRead(MappedByteBuffer map, int size) {
        int limit = map.limit();
        int position = map.position();
        if (position == limit) {
            return null;
        }
        if (limit - position > size) {
            byte[] array = new byte[size];
            map.get(array);
            return array;
        } else {// 最后一次读取数据
            byte[] array = new byte[limit - position];
            map.get(array);
            return array;
        }
    }
}
