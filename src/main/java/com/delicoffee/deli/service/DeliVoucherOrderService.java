package com.delicoffee.deli.service;

import com.delicoffee.deli.model.entity.DeliVoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher_order】的数据库操作Service
* @createDate 2023-09-07 10:56:54
*/
public interface DeliVoucherOrderService extends IService<DeliVoucherOrder> {

    Long seckillVoucher(Long voucherId);

    void createVoucherOrder(DeliVoucherOrder voucherOrder);
}
