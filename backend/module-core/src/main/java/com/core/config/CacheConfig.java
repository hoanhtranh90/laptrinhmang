/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.config;

import com.core.utils.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
//import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author DELL
 */
//@RefreshScope
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    @Value(value = "${ehcache.config.name.maxEntries}")
    private Long maxEntries;
    @Value(value = "${ehcache.config.name.timeLive}")
    private Long timeLive;

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfig("${ehcache.config.name.plance}"));
        config.addCache(cacheConfig("${ehcache.config.name.listBts}"));
        config.addCache(cacheConfig("${ehcache.config.name.listCell}"));
        config.addCache(cacheConfig("${ehcache.config.name.listRadio}"));
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    private CacheConfiguration cacheConfig(String name) {
        CacheConfiguration config = new CacheConfiguration();
        config.setName(name);
        config.setMaxEntriesLocalHeap(maxEntries);
        config.setMemoryStoreEvictionPolicyFromObject(MemoryStoreEvictionPolicy.LFU);
        if (StringUtils.isTrue(timeLive)) {
            config.setTimeToLiveSeconds(timeLive);
        }
        return config;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }

}
