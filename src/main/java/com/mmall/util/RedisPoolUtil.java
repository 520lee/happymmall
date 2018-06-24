package com.mmall.util;
/*
    author: king
    date: 2018/6/23
*/

import com.mmall.common.RedisPool;
import redis.clients.jedis.Jedis;

public class RedisPoolUtil {
    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        result = jedis.set(key, value);
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String setEx(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        result = jedis.setex(key, exTime, value);
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long setExpire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;

        jedis = RedisPool.getJedis();
        result = jedis.expire(key, exTime);
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        jedis = RedisPool.getJedis();
        result = jedis.del(key);
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        result = jedis.get(key);
        RedisPool.returnResource(jedis);
        return result;
    }
}
