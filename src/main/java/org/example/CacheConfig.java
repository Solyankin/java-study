package org.example;

import org.example.cache.users.UserCacheExternal;
import org.example.cache.users.UserCacheInternal;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.<Cache>asList(
                new UserCacheExternal(),
                new UserCacheInternal()
        ));
        return cacheManager;
    }
}
