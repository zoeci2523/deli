package com.delicoffee.deli.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CategoryVO implements Serializable {

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

    /**
     * 子目录
     */
    private List<CategoryVO> child_category = new ArrayList<>();

}
