/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.config;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.cache.CacheManager;

/**
 * @author DELL
 */
@Component
@Slf4j
public class CommonCache {

    @Autowired
    private CacheManager cacheManager;

    public Collection<String> getCacheList() {
        return cacheManager.getCacheNames();
    }

    public Boolean isCacheExist(String cacheName) {
        Collection<String> cacheList = getCacheList();
        return (!cacheList.isEmpty() && cacheList.contains(cacheName)) ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean clearCache(String cacheName) {
        if (isCacheExist(cacheName)) {
            cacheManager.getCache(cacheName).clear();
        }
        return Boolean.TRUE;
    }

    public Boolean clearAllCache() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> {
            cacheManager.getCache(name).clear();
        });
        return Boolean.TRUE;
    }
}
