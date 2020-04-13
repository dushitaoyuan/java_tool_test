package com.taoyuanx.sortexample.utils;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.UUID;

/**
 * @author dushitaoyuan
 * @date 2020/4/13
 */
public class HelpUtil {
    public static String getRandomChunk() {
        String chunkFilePrefix = "chunk_num_";
        return chunkFilePrefix + UUID.randomUUID().toString().replaceAll("-", "") + ".chunk";
    }
    public static String getRandomResultChunk() {
        String chunkFilePrefix = "chunk_num_result";
        return chunkFilePrefix + UUID.randomUUID().toString().replaceAll("-", "") + ".result";
    }
    public static void cleanBuffer(Object buffer) {
        if (buffer != null) {
            AccessController.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                        if(cleaner!=null){
                            cleaner.clean();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }


    }
}
