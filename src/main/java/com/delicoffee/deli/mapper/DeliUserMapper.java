package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
* @author fengxiaoha
* @description 针对表【deli_user(用户表)】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliUser
*/

@Mapper
public interface DeliUserMapper extends BaseMapper<DeliUser> {

    int insertSelective(DeliUser record);

    DeliUser selectByName(String userName);

    //多参数需要写明@Param
    DeliUser selectLogin(@Param("username")String username, @Param("password")String password);

    DeliUser selectOneByMobile(String mobile);
}




