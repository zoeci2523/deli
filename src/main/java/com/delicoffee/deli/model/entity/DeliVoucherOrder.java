package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName deli_voucher_order
 */
@TableName(value ="deli_voucher_order")
@Data
public class DeliVoucherOrder implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Integer user_id;

    private Long voucher_id;

    private Integer pay_type;

    private Integer status;

    private Date create_time;

    private Date pay_time;

    private Date use_time;

    private Date refund_time;

    private Date update_time;

    private static final long serialVersionUID = 1L;
}