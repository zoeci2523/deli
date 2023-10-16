package com.delicoffee.deli.controller;

import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.model.request.CreateOrderReq;
import com.delicoffee.deli.service.DeliOrderService;
import com.delicoffee.deli.vo.OrderVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * order controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    DeliOrderService orderService;

    /**
     * 创建订单
     * @param createOrderReq
     * @return
     */
    @PostMapping("/create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    /**
     * 查看单个订单详情
     * @param orderNo
     * @return
     */
    @GetMapping("/detail")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    /**
     * 查看所有订单信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 删除订单
     * @param orderNo
     * @return
     */
    @GetMapping("/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 生成支付二维码
     * @param orderNo
     * @return
     */
    @GetMapping("/code")
    public ApiRestResponse code(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    /**
     * 支付接口
     * @param orderNo
     * @return
     */
    @GetMapping("/pay")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }


}
