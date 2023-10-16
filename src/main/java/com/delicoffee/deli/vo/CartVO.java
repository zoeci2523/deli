package com.delicoffee.deli.vo;

import lombok.Data;
import java.io.Serializable;


/**
 * 返回给前端的购物车封装类
 */

@Data
public class CartVO implements Serializable {
    /**
     * 购物车id
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer product_id;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 是否已勾选：0代表未勾选，1代表已勾选
     */
    private Integer selected;

    /**
     * 商品价格
     */
    private Integer price;

    /**
     * 商品总价
     */
    private Integer total_price;

    /**
     * 商品名称
     */
    private String product_name;

    /**
     * 商品图片
     */
    private String product_image;

    private static final long serialVersionUID = 1L;
}