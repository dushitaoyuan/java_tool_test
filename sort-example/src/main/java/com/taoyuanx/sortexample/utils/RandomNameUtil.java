package com.taoyuanx.sortexample.utils;

import java.util.UUID;

/**
 * @author dushitaoyuan
 * @date 2020/4/13
 */
public class RandomNameUtil {
    public static String getRandomChunk() {
        String chunkFilePrefix = "chunk_num_";
        return chunkFilePrefix + UUID.randomUUID().toString().replaceAll("-", "") + ".chunk";
    }
}
