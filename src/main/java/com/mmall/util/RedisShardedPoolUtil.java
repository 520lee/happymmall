package com.mmall.util;
/*
    author: king
    date: 2018/6/23
*/


import com.mmall.common.RedisShardedPool;
import redis.clients.jedis.ShardedJedis;

public class RedisShardedPoolUtil {
    public static String set(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        result = jedis.set(key, value);
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String setEx(String key, String value, int exTime){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        result = jedis.setex(key, exTime, value);
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long setExpire(String key, int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        result = jedis.expire(key, exTime);
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        result = jedis.del(key);
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        result = jedis.get(key);
        RedisShardedPool.returnResource(jedis);
        return result;
    }
}
