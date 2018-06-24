package com.mmall.common;
/*
    author: king
    date: 2018/6/21
*/

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port", "6379"));
    private static String redis1Host = PropertiesUtil.getProperty("redis1.ip", "118.190.158.81");

    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port", "6380"));
    private static String redis2Host = PropertiesUtil.getProperty("redis2.ip", "118.190.158.81");

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
        JedisShardInfo info1 = new JedisShardInfo(redis1Host, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Host, redis2Port, 1000 * 2);
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>(2);
        shardInfos.add(info1);
        shardInfos.add(info2);
        pool = new ShardedJedisPool(config, shardInfos, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis){ pool.returnResource(jedis); }

}
