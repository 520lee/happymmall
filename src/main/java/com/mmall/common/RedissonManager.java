package com.mmall.common;
/*
    author: king
    date: 2018/6/27
*/

import com.mmall.util.PropertiesUtil;
import org.redisson.Redisson;
import org.redisson.config.Config;

import javax.annotation.PostConstruct;

public class RedissonManager {

    private Redisson redisson = null;

    private Config config = new Config();

    public Redisson getRedisson() {
        return redisson;
    }

    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port", "6379"));
    private static String redis1Host = PropertiesUtil.getProperty("redis1.ip", "118.190.158.81");

    @PostConstruct
    private void init(){
        config.useSingleServer().setAddress(redis1Host +":"+ redis1Port);
        redisson = (Redisson) Redisson.create(config);
    }
}
