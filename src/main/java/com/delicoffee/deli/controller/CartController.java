package com.delicoffee.deli.controller;

import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.service.DeliCartService;
import com.delicoffee.deli.vo.CartVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    DeliCartService cartService;

    /**
     * 获得购物车列表
     * @return
     */
    @GetMapping("/listAll")
    public ApiRestResponse listAll(){
        List<CartVO> cartVOList = cartService.listAll();
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 添加商品到购物车
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count){
        // 获得更新后的购物车列表
        List<CartVO> cartVOList = cartService.add(productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 更新购物车里的商品
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count){
        List<CartVO> cartVOList = cartService.update(productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 删除购物车里的商品
     * @param productId
     * @return
     */
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam Integer productId){
        List<CartVO> cartVOList = cartService.delete(productId);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 选中/不选中购物车里的商品
     * @param productId
     * @param selected
     * @return
     */
    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected){
        List<CartVO> cartVOList = cartService.selectOrNot(productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 全部选中/不选中购物车里的商品
     * @param selected
     * @return
     */
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected){
        List<CartVO> cartVOList = cartService.selectAllOrNot(selected);
        return ApiRestResponse.success(cartVOList);
    }


}
