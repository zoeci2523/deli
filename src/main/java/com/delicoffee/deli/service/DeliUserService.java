package com.delicoffee.deli.service;

import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.model.entity.DeliUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author fengxiaoha
* @description 针对表【deli_user(用户表)】的数据库操作Service
* @createDate 2023-09-03 07:31:51
*/
public interface DeliUserService extends IService<DeliUser> {

    void register(String username, String password, String mobile);

    DeliUser login(String username, String password) throws DeliCoffeeException;

    boolean checkMobileRegistered(String mobile);

}
