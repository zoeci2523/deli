package com.delicoffee.deli.service;

import com.delicoffee.deli.model.entity.DeliOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicoffee.deli.model.request.CreateOrderReq;
import com.delicoffee.deli.vo.OrderVO;
import com.github.pagehelper.PageInfo;

/**
* @author fengxiaoha
* @description 针对表【deli_order(订单表;)】的数据库操作Service
* @createDate 2023-09-03 07:31:51
*/
public interface DeliOrderService extends IService<DeliOrder> {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    void pay(String orderNo);
}
