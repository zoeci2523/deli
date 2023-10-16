package com.delicoffee.deli.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回给前端的订单的商品封装类
 */

@Data
public class OrderItemVO implements Serializable {

    /**
     * 归属订单id
     */
    private String order_no;

    /**
     * 商品id
     */
    private Integer product_id;

    /**
     * 商品名称
     */
    private String product_name;

    /**
     * 商品图片
     */
    private String product_img;

    /**
     * 单价（下单时的快照）
     */
    private Integer unit_price;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品总价
     */
    private Integer total_price;

    private static final long serialVersionUID = 1L;
}