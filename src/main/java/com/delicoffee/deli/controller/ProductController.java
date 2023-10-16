package com.delicoffee.deli.controller;

import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.model.request.ProductListReq;
import com.delicoffee.deli.service.DeliProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 商品controller
 */

@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    DeliProductService productService;

    @GetMapping("/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        DeliProduct product = productService.detail(id);
        return ApiRestResponse.success(product);
    }


    @GetMapping("/list")
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
