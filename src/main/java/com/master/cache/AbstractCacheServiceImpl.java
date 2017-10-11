package com.master.cache;

import com.master.lock.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaoxia on 2017/9/1 15:24.
 */
public abstract class AbstractCacheServiceImpl<K, V> implements CacheService<K,V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCacheServiceImpl.class);

    private static final long serialVersionUID = 9156331325613073671L;

    @Autowired
    protected RedisTemplate<K, V> redisTemplate;


    @Override
    public Boolean keyExists(K key) {
        long start = System.currentTimeMillis();
        boolean bol = redisTemplate.hasKey(key);
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.keyExists执行时间超过" + ms + "ms");
        }
        return bol;
    }

    @Override
    public Boolean keyExists(K key, V value) {
        long start = System.currentTimeMillis();
        boolean result = false;
        if (redisTemplate.hasKey(key)) {
            if (value.equals(get(key))) {
                result = true;
            }
        }
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.keyExists执行时间超过" + ms + "ms");
        }
        return result;
    }

    @Override
    public V get(K key) {
        long start = System.currentTimeMillis();
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        V v = valueOper.get();
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.get(K)执行时间超过" + ms + "ms");
        }
        return v;
    }

    @Override
    public void set(K key, V value) {
        logger.debug("set mess key={" + key + "}, value={" + value + "}");
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(value)) {
            logger.debug("尝试写入的value为空, k=" + key + " v=" + value);
            return;
        }
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        valueOper.set(value);
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.set(k v)执行时间超过" + ms + "ms");
        }
    }

    @Override
    public void set(K key, V value, long expiredTime) {
        long start = System.currentTimeMillis();
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        if (expiredTime >= 0) {
            valueOper.set(value, expiredTime, TimeUnit.MILLISECONDS);
        }
        long end = System.currentTimeMillis();
        long ms = end - start;
        if(ms > 100){
            logger.warn("CacheService.set(k v time)执行时间超过"+ms+"ms");
        }
    }

    @Override
    public void delete(K key) {
        long start = System.currentTimeMillis();
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
            logger.debug("CacheService.delete删除 "+key+"ms");
        }
        long end = System.currentTimeMillis();
        long ms = end - start;
        if(ms > 100){
            logger.warn("CacheService.delete执行时间超过"+ms+"ms");
        }
    }

    @Override
    @Deprecated
    public void deleteByPrex(String prex) {
        long start = System.currentTimeMillis();
        Set<K> keys = redisTemplate.keys((K) (prex+"*"));
        redisTemplate.delete(keys);
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.deleteByPrex执行时间超过" + ms + "ms");
        }
    }

    @Override
    public Integer getByPrexSize(String prex) {
        long start = System.currentTimeMillis();
        Set<K> keys = redisTemplate.keys((K) (prex + "*"));
        if (null == keys) {
            return 0;
        }
        int size = keys.size();
        long end = System.currentTimeMillis();
        long ms = end - start;
        if (ms > 100) {
            logger.warn("CacheService.getByPrexSize执行时间超过" + ms + "ms");
        }
        return size;
    }

    @Override
    public Set<String> getKeysByPrex(String prex) {
        return (Set<String>) redisTemplate.keys((K) (prex + "*"));
    }

    @Override
    public RedisLock getLock(String lockName) throws Exception {
        RedisLock lock = new RedisLock(redisTemplate, lockName);
        if(lock.lock() ){
            return lock;
        }else {
            throw new Exception("获取分布式锁失败");
        }
    }

    @Override
    public Long listSize(K key) {
        BoundValueOperations<K,V> valueOperations = redisTemplate.boundValueOps(key);
        return valueOperations.size();
    }

    @Override
    public V rightPop(K key) {
        BoundListOperations<K,V> listOperations = redisTemplate.boundListOps(key);
        return listOperations.rightPop();
    }

    @Override
    public V rightPop(K key, long timeout) {
        BoundListOperations<K, V> boundListOps = redisTemplate.boundListOps(key);
        return boundListOps.rightPop(timeout,  TimeUnit.MILLISECONDS);
    }

    @Override
    public void leftPush(K key, V value) {
        BoundListOperations<K, V> boundListOps = redisTemplate.boundListOps(key);
        boundListOps.leftPush(value);
    }

    @Override
    public void leftPushAll(K key, V... values) {
        BoundListOperations<K, V> boundListOps = redisTemplate.boundListOps(key);
        boundListOps.leftPushAll(values);
    }
}
