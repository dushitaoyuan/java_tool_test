package com.taoyuanx.sortexample;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

public class CReferenceText {

    public static void main(String[] args) {
        LinkedHashMap linkedHashMap = new LinkedHashMap<>();

        linkedHashMap.put("1", "1");
        linkedHashMap.remove("1");
        linkedHashMap.get("1");
    }
    @Test
    public  void sortTest() {
        int[] numArray = new int[]{7, 1, 2, 6, 9, 10,11};
        int temp = 0;
        for (int i = numArray.length-1; i >0; i--) {
            for (int j = 0; j < i; j++) {
                if(numArray[j]>numArray[j+1]){
                    temp=numArray[j];
                    numArray[j]=numArray[j+1];
                    numArray[j+1]=temp;
                }
            }
        }
        System.out.println(Arrays.toString(numArray));
    }

}
