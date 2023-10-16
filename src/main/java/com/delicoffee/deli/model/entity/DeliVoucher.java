package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName deli_voucher
 */
@TableName(value ="deli_voucher")
@Data
public class DeliVoucher implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer product_id;

    private String title;

    private String sub_title;

    private String rules;

    private Integer stock;

    private Long pay_value;

    private Long actual_value;

    private Integer type;

    private Integer status;

    private Date begin_time;

    private Date end_time;

    private Date create_time;

    private Date update_time;

    private static final long serialVersionUID = 1L;
}