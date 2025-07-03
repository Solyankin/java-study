package org.example.cache.users;

import org.springframework.cache.Cache;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("ALL")
abstract class UserCache<K, V> implements Cache {
    private final ConcurrentMap<K, Optional<V>> cache = new ConcurrentHashMap<>();
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
        Optional<V> value = cache.get((K) key);
        return value != null && value.isPresent() ? () -> value : null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Optional<V> value = cache.get((K) key);
        return value != null && value.isPresent() ? (T) value : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Optional<V> value = cache.get((K) key);
        if (value == null) {
            try {
                value = (Optional<V>) valueLoader.call();
                cache.put((K) key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (value.isEmpty()) {
            return null;
        }

        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        if (value != null) {
            cache.put((K) key, Optional.of((V) value));
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