package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.model.entity.DeliUser;
import com.delicoffee.deli.service.DeliUserService;
import com.delicoffee.deli.mapper.DeliUserMapper;
import com.delicoffee.deli.util.MD5Utils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
* @author fengxiaoha
* @description 针对表【deli_user(用户表)】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliUserServiceImpl extends ServiceImpl<DeliUserMapper, DeliUser>
    implements DeliUserService{

    @Autowired
    DeliUserMapper userMapper;

    @Override
    public void register(String username, String password, String mobile) {
        //查询用户名是否存在，不允许重名
        DeliUser result = userMapper.selectByName(username);
        if (result != null) {
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NAME_EXISTED);
        }

        //写入数据库
        DeliUser user = new DeliUser();
        user.setUsername(username);
        user.setMobile(mobile);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public DeliUser login(String username, String password) throws DeliCoffeeException{
        // 拿到加密后的密码
        String md5Pwd = null;
        try{
            md5Pwd = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DeliUser user = userMapper.selectLogin(username, md5Pwd);
        if (user == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public boolean checkMobileRegistered(String mobile){
        DeliUser user = userMapper.selectOneByMobile(mobile);
        if (user != null){
            return false;
        }
        return true;
    }


}




