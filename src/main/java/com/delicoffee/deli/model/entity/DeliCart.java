package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 购物车
 * @TableName deli_cart
 */
@TableName(value ="deli_cart")
@Data
public class DeliCart implements Serializable {
    /**
     * 购物车id
     */
    @TableId(type = IdType.AUTO)
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