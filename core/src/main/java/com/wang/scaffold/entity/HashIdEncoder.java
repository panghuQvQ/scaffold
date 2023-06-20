package com.wang.scaffold.entity;


import com.wang.scaffold.utils.Hashids;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class HashIdEncoder implements MaskId.IdEncoder<String, Integer> {

    private static final Map<_CacheKey, HashIdEncoder> _caches = new ConcurrentHashMap<>();

    private final Hashids delegate;

    private HashIdEncoder(String salt) {
        delegate = new Hashids(salt);
    }

    private HashIdEncoder(String salt, int minHashLength) {
        delegate = new Hashids(salt, minHashLength);
    }

    public static HashIdEncoder getInstance(String salt) {
        return getInstance(salt, 0);
    }

    public static HashIdEncoder getInstance(String salt, int minHashLength) {
        _CacheKey cacheKey = new _CacheKey();
        cacheKey.salt = salt;
        cacheKey.minHashLength = minHashLength;
        return _caches.computeIfAbsent(cacheKey, (key) -> new HashIdEncoder(salt, minHashLength));
    }

    @Override
    public String encode(Integer id) {
        return delegate.encode(id);
    }

    @Override
    public Integer decode(String encodedId) {
        return (int) delegate.decode(encodedId)[0];
    }
}

class _CacheKey {
    String salt;
    int minHashLength;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _CacheKey cacheKey = (_CacheKey) o;
        return minHashLength == cacheKey.minHashLength && Objects.equals(salt, cacheKey.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salt, minHashLength);
    }
}
