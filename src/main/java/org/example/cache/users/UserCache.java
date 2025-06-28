package org.example.cache.users;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("ALL")
abstract class UserCache<K, V> implements Cache {
    private final ConcurrentMap<K, V> cache = new ConcurrentHashMap<>();
    private final String name;

    public UserCache(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }


    @Override
    public ValueWrapper get(Object key) {
        V value = cache.get((K) key);
        return value != null ? () -> value : null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        V value = cache.get((K) key);
        return value != null ? (T) value : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        V value = cache.get((K) key);
        if (value == null) {
            try {
                value = (V) valueLoader.call();
                cache.put((K) key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        if (value != null) {
            cache.put((K) key, (V) value);
        }
    }

    @Override
    public void evict(Object key) {
        cache.remove((K) key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}