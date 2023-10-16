package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品分类
 * @TableName deli_category
 */
@TableName(value ="deli_category")
@Data
public class DeliCategory implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类目录名称
     */
    private String name;

    /**
     * 分类目录级别，例如1代表一级，2代表二级，3代表三级
     */
    private Integer type;

    /**
     * 父id，也就是上一级目录的id，如果是一级目录，那么父id为0
     */
    private Integer parent_id;

    /**
     * 目录展示时的排序
     */
    private Integer order_num;

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