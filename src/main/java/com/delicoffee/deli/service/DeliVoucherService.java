package com.delicoffee.deli.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.model.entity.DeliVoucher;
import org.springframework.transaction.annotation.Transactional;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher】的数据库操作Service
* @createDate 2023-09-07 10:56:54
*/
public interface DeliVoucherService extends IService<DeliVoucher> {
    ApiRestResponse queryVoucherOfProduct(Long productId);

    @Transactional
    void addSeckillVoucher(DeliVoucher voucher);

}
