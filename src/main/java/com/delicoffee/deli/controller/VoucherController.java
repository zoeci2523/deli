package com.delicoffee.deli.controller;


import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.model.entity.DeliVoucher;
import com.delicoffee.deli.service.DeliVoucherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 优惠券controller
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private DeliVoucherService voucherService;

    /**
     * 新增普通券
     *
     * @param voucher 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public ApiRestResponse addVoucher(@RequestBody DeliVoucher voucher) {
        voucherService.save(voucher);
        return ApiRestResponse.success(voucher.getId());
    }

    /**
     * 新增秒杀券
     *
     * @param voucher 优惠券信息，包含秒杀信息
     * @return 优惠券id
     */
    @PostMapping("seckill")
    public ApiRestResponse addSeckillVoucher(@RequestBody DeliVoucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return ApiRestResponse.success(voucher.getId());
    }

    /**
     * 查询商品的优惠券列表
     */
    @GetMapping("/list/{productId}")
    public ApiRestResponse queryVoucherOfProduct(@PathVariable("productId") Long productId) {
        return voucherService.queryVoucherOfProduct(productId);
    }
}
