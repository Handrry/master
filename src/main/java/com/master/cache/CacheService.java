package com.master.cache;

import com.master.lock.RedisLock;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by xiaoxia on 2017/9/1 14:23.
 */
public interface CacheService<K,V> extends Serializable{

    public enum REDIS_KEY{
        /** 分布锁*/
        lock_,
        /** 用户*/
        user_;
    }

    /**
     * key是否存在
     * @param key
     * @return
     */
    Boolean keyExists(K key);

    /**
     * key-value 是否存在
     * @param key
     * @param value
     * @return
     */
    Boolean keyExists(K key, V value);

    /**
     * 获取缓存值
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 缓存指定值
     * @param key
     * @param value
     */
    void set(K key, V value);

    /**
     * 缓存指定值，设置过期时间
     * @param key
     * @param value
     * @param expiredTime
     */
    void set(K key, V value, long expiredTime);

    /**
     * 删除缓存数据
     * @param key
     */
    void delete(K key);

    /**
     * deleteByPrex: 模糊删除以prex参数为前缀的key数据<br/>
     * 比如要删除user_开头的数据，请传入字符串user_ <br/>
     * 但考虑到性能问题，可能存在大量数据时慎用
     */
    @Deprecated
    void deleteByPrex(String prex);

    /**
     * like查询条数
     * @param prex
     * @return
     */
    Integer getByPrexSize(String prex);

    /**
     * @param prex
     * @return
     */
    Set<String> getKeysByPrex(String prex);

    /**
     * 获取锁
     *
     * @param lockName
     * @return
     */
    RedisLock getLock(String lockName) throws Exception;

    /**
     * list条目数
     * @param key
     * @return
     */
    Long listSize(K key);

    /**
     * 移出并获取列表的最后一个元素
     *
     * @param key
     * @return
     */
    V rightPop(K key);

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key
     * @param timeout
     * @return
     */
    V rightPop(K key, long timeout);

    /**
     * 将一个或多个值插入到列表头部
     *
     * @param key
     * @param value
     */
    void leftPush(K key, V value);

    /**
     * 加入批量
     *
     * @param key
     * @param values
     */
    void leftPushAll(K key, V... values);

    /**
     * 清空缓存
     */
    void flushDb();

}
