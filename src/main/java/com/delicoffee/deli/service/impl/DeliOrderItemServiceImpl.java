package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.model.entity.DeliOrderItem;
import com.delicoffee.deli.service.DeliOrderItemService;
import com.delicoffee.deli.mapper.DeliOrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author fengxiaoha
* @description 针对表【deli_order_item(订单的商品表 )】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliOrderItemServiceImpl extends ServiceImpl<DeliOrderItemMapper, DeliOrderItem>
    implements DeliOrderItemService{

}




