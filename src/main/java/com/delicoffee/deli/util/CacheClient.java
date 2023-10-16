package com.delicoffee.deli.util;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.delicoffee.deli.common.Constant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //将java对象序列化为json并存储在string类型的key中，并设置TTL过期时间
    public void set(String key, Object value, Long time, TimeUnit unit){
        //需要将传入的value序列化
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    //将java对象序列化为json并存储在string类型的key中，设置逻辑过期时间，用于处理缓存击穿
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){
        //设置逻辑过期对象
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    //根据指定key查询缓存，并反序列化为指定类型，通过缓存空值解决缓存穿透
    //不确定输出参数的类型，使用范型
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type,
                                          Function<ID, R> dbCallBack, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        // 从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断key是否存在在缓存中
        if (StrUtil.isNotBlank(json)){
            // 存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        // 判断命中的是否是空值(因为防止缓存穿透设置的是空值)
        if (json != null){
            System.out.println("缓存查询结果为空值");
            return null; //返回一个错误信息
        }

        System.out.println("缓存查询不到，查询数据库中...");
        // 不存在，根据id查询数据库
        // 需要调用者告诉我们查询的逻辑 -> 函数式编程
        R r = dbCallBack.apply(id);

        // 数据库不存在该值，返回错误
        if (r == null){
            //将空值写入redis
            stringRedisTemplate.opsForValue().set(key, "", Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
            return null;
        }
        this.set(key, r, time, unit);
        return r;
    }

    // 用于异步写入数据库的线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);


    //根据指定key查询缓存，并反序列化为指定类型，通过逻辑过期解决缓存击穿
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type,
                                          Function<ID, R> dbCallBack, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        // 从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断key是否存在在缓存中
        if (StrUtil.isBlank(json)){
            // 不存在，返回一个错误信息
            return null;
        }
        // 命中，需要先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();

        // 通过逻辑时间判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回结果
            return r;
        }
        // 已过期，需要缓存重建
        // 先获取互斥锁
        String lockKey = Constant.LOCK_CATEGORY_KEY +id;
        boolean isLock = tryLock(lockKey);
        // 判断是否获取锁成功
        System.out.println("数据已过期，尝试更新...");
        if (isLock){
            System.out.println("数据已过期，正在更新中...");
            // 获取锁成功，是第一个进行缓存重建的人，开启独立进程实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try{
                    // 查询数据库
                    System.out.println("查询数据库中...");
                    R r1 = dbCallBack.apply(id);
                    // 写入redis
                    this.setWithLogicalExpire(key, r1, time, unit);
                    System.out.println("更新已完成");
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        System.out.println("数据已过期，更新失败");
        // 返回过期的数据
        return r;
    }

    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1",10, TimeUnit.SECONDS);
        // 需要使用BooleanUtil来封装，否则拆箱不太对
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }
}
