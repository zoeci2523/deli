package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品表
 * @TableName deli_product
 */
@TableName(value ="deli_product")
@Data
public class DeliProduct implements Serializable {
    /**
     * 商品主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 产品图片,相对路径地址
     */
    private String image;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 分类id
     */
    private Integer category_id;

    /**
     * 价格,单位-分
     */
    private Integer price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品上架状态：0-下架，1-上架
     */
    private Integer status;

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