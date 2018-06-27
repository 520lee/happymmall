package com.mmall.task;
/*
    author: king
    date: 2018/6/26
*/

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){
        int minutes = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.minutes", "30"));
        iOrderService.closeOrder(minutes);
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setNxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setNxResult != null && (setNxResult.intValue() == 1)){
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3(){
        Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setNxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setNxResult != null && (setNxResult.intValue() == 1)){
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && (System.currentTimeMillis() > Long.parseLong(lockValueStr))){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
                if (getSetResult == null || StringUtils.equals(lockValueStr, getSetResult)){
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }
        }
    }

    private void closeOrder(String lockName){
        RedisShardedPoolUtil.setExpire(lockName, 5);
        iOrderService.closeOrder(Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.minutes", "30")));
        RedisShardedPoolUtil.del(lockName);
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4(){
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        Boolean getLock =false;
        try {
            if (getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){
                int minutes = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.minutes", "30"));
                iOrderService.closeOrder(minutes);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!getLock){
                return;
            }
            lock.unlock();
        }
    }
}
