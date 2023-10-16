package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_order(订单表;)】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliOrder
*/
@Mapper
public interface DeliOrderMapper extends BaseMapper<DeliOrder> {

    DeliOrder selectByOrderNo(String orderNo);

    List<DeliOrder> selectForCustomer(Integer userId);

}




