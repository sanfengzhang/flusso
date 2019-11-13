package com.hanl.data.common;


import com.github.benmanes.caffeine.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: Hanl
 * @date :2019/8/6
 * @desc:
 */
public class EnhanceCache {

    private static final Logger log = LoggerFactory.getLogger(EnhanceCache.class);

    public static <K, V> LoadingCache<K, V> create(int capacity, Duration expireAfterWrite, Duration expireAfterAccess, CacheLoader<K, V> cacheLoader) {

        LoadingCache<K, V> cache = Caffeine.newBuilder().initialCapacity(capacity).expireAfterWrite(expireAfterWrite).
                expireAfterAccess(expireAfterAccess).build(cacheLoader);

        return cache;
    }


    public static <K, V> LoadingCache<K, V> create(int capacity, CacheLoader<K, V> cacheLoader) {

        LoadingCache<K, V> cache = Caffeine.newBuilder().initialCapacity(capacity).build(cacheLoader);

        return cache;
    }

    public static <K, V> LoadingCache<K, V> create(int capacity, CacheLoader<K, V> cacheLoader, ScheduledExecutorService scheduledExecutorService, Callable<Map<K, V>> callable, long delay, long period) {

        LoadingCache<K, V> cache = Caffeine.newBuilder().initialCapacity(capacity).build(cacheLoader);
        if (null != scheduledExecutorService) {
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<K, V> result = callable.call();
                        if (result != null && result.size() != 0) {
                            cache.putAll(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, delay, period, TimeUnit.SECONDS);
        }
        return cache;
    }

    public static <K, V> AsyncLoadingCache<K, V> create(int capacity, AsyncCacheLoader<K, V> cacheLoader) {

        AsyncLoadingCache<K, V> cache = Caffeine.newBuilder().initialCapacity(capacity).buildAsync(cacheLoader);

        return cache;
    }

    public static <K, V> AsyncLoadingCache<K, V> create(int capacity, Duration expireAfterWrite, Duration expireAfterAccess, AsyncCacheLoader<K, V> cacheLoader) {

        AsyncLoadingCache<K, V> cache = Caffeine.newBuilder().initialCapacity(capacity).expireAfterWrite(expireAfterWrite).
                expireAfterAccess(expireAfterAccess).buildAsync(cacheLoader);

        return cache;
    }
}
