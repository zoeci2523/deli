package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单的商品表 
 * @TableName deli_order_item
 */
@TableName(value ="deli_order_item")
@Data
public class DeliOrderItem implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}