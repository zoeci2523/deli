package com.delicoffee.deli.mapper;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.delicoffee.deli.model.entity.DeliOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_order_item(订单的商品表 )】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliOrderItem
*/
@Mapper
public interface DeliOrderItemMapper extends BaseMapper<DeliOrderItem> {
    List<DeliOrderItem> selectByOrderNo(String ordrNo);

}




