package com.delicoffee.deli.once;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * Deli平台商品信息
 * 从excel引入，完成excel和java类对象字段的映射
 */
@Data
public class DeliProductInfo {

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

}
