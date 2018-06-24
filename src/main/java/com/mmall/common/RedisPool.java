package com.mmall.common;
/*
    author: king
    date: 2018/6/21
*/

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static JedisPool pool;
    private static Integer port = Integer.parseInt(PropertiesUtil.getProperty("redis.port", "6379"));
    private static String host = PropertiesUtil.getProperty("redis.ip", "118.190.158.81");
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    private static Integer maxIdel = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idel", "10"));
    private static Integer minIdel = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idel", "2"));
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return"));

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdel);
        config.setMinIdle(minIdel);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setMaxWaitMillis(1000*2);
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config, host, port);
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis){ pool.returnResource(jedis); }
}
