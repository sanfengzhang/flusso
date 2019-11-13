package com.hanl.datamgr.support;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/14
 * @desc:
 */
public class DataTransformUtils<K, V> {

    //FIXME
    public <K, V> Map<K, List<V>> convert(String filedName, Iterator<V> it) throws IllegalAccessException {
        Map<K, List<V>> result = new HashMap<>();
        if (null != it) {
            while (it.hasNext()) {
                V data = it.next();
                Class clazz = (Class) data.getClass();
                Field f = null;
                try {
                    f = clazz.getDeclaredField(filedName);
                } catch (NoSuchFieldException e) {
                    try {
                        f = clazz.getSuperclass().getDeclaredField(filedName);
                    } catch (NoSuchFieldException ex) {
                        ex.printStackTrace();
                    }
                }
                f.setAccessible(true);
                K key = (K) f.get(data);
                List<V> list = result.get(key);
                if (null == list) {
                    list = new ArrayList<>();
                    result.put(key, list);
                }
                list.add(data);
            }
        }
        return result;
    }

    public void sort(String filedName, List<V> list) throws IllegalAccessException {
        Map<K, List<V>> result = new HashMap<>();
        if (null != list && list.size() > 0) {
            list.sort(new Comparator<V>() {
                @Override
                public int compare(V o1, V o2) {

                    return 0;
                }
            });
        }
    }
}
