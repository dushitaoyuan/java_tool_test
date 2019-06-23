package com.taoyuanx.dozer;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 不要多实例使用
 */
public class CBeanMapper {
    public  static  final  String mappings[]={"dozer.xml"};
    private static  Mapper mapper=new DozerBeanMapper(Lists.newArrayList(mappings));


    /**
     * 简单的复制出新类型对象.
     */
    public static  <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 增加 后处理
     * @return
     */
    public static <S, D> D map(S source, Class<D> destinationClass,CBeanAfterHandler<S,D> beanAfterHandler) {
        D d=mapper.map(source, destinationClass);
        beanAfterHandler.handle(source,d);
        return d;
    }

    /**
     * 简单的复制出新对象ArrayList
     */
    public static  <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destionationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                destionationList.add(mapper.map(source, destinationClass));
            }
        }
        return destionationList;
    }
    /**
     * 增加 后处理
     * @return
     */
    public static  <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass,CBeanAfterHandler<S,D>  beanAfterHandler) {
        List<D> destionationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                destionationList.add(map(source,destinationClass,beanAfterHandler));
            }
        }
        return destionationList;
    }

    /**
     * 简单复制出新对象数组
     */
    public static  <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);

        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
                i++;
            }
        }

        return destinationArray;
    }

    /**
     * 增加 后处理
     * @return
     */
    public  static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass,CBeanAfterHandler<S,D>  beanAfterHandler) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);

        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = map(sourceArray[i],destinationClass,beanAfterHandler);
                i++;
            }
        }
        return destinationArray;
    }
}