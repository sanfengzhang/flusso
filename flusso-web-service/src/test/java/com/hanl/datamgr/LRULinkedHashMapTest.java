package com.hanl.datamgr;

import java.util.LinkedHashMap;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
public class LRULinkedHashMapTest {
}

class LRULinkedHashMap<K, V> extends LinkedHashMap {
    public LRULinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {

        super(initialCapacity, loadFactor, accessOrder);
    }
}
