package com.delicoffee.deli.controller;


import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.service.DeliVoucherOrderService;
import com.delicoffee.deli.service.DeliVoucherService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/voucher/order")
public class VoucherOrderController {

    @Resource
    private DeliVoucherOrderService voucherOrderService;

    @PostMapping("seckill/{id}")
    public ApiRestResponse seckillVoucher(@PathVariable("id") Long voucherId) {
        Long orderId = voucherOrderService.seckillVoucher(voucherId);
        int r = orderId.intValue();
        if (r == 1){
            ApiRestResponse.error(DeliCoffeeExceptionEnum.NOT_ENOUGH);
        }else if (r == 2){
            ApiRestResponse.error(DeliCoffeeExceptionEnum.DOUBLE_ORDER);
        }
        return ApiRestResponse.success(orderId);
    }
}
