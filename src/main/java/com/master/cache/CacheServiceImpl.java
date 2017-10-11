package com.master.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by xiaoxia on 2017/9/2 9:53.
 */
@Component
public class CacheServiceImpl extends AbstractCacheServiceImpl{

    @Override
    public void flushDb() {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.flushDb();
                return true;
            }
        });
    }
}
