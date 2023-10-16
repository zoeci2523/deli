package com.delicoffee.deli.service.impl;

import com.delicoffee.deli.service.DeliMobileService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DeliMobileServiceImpl implements DeliMobileService {
    private static final Logger log = LoggerFactory.getLogger(DeliMobileServiceImpl.class);
    @Override
    public boolean checkMobileAndCode(String mobile, String verificationCode){
        //默认连接到本地的client
        RedissonClient client = Redisson.create();
        RBucket<String> bucket = client.getBucket(mobile);
        boolean exist = bucket.isExists();
        if (exist){
            String code = bucket.get();
            // redis存储的验证码和用户传过来的一致，则校验通过
            if (code.equals(verificationCode)) return true;
        }
        return false;
    }

    @Override
    public boolean saveMobileToRedis(String mobile, String code){
        //默认连接到本地的client
        RedissonClient client = Redisson.create();
        RBucket<String> bucket = client.getBucket(mobile);
        boolean exist = bucket.isExists();
        if (!exist){
            // 第一次访问，放入验证码，同时设置过期时间
            bucket.set(code, 60, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public void sendCode(String mobile, String code){
        // 暂不实现，因为需要去专门的平台实现
        log.info("发送短信验证码成功，验证码：{}", code);
        return;
    }
}
