package com.delicoffee.deli.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName deli_seckill_voucher
 */
@TableName(value ="deli_seckill_voucher")
@Data
public class DeliSeckillVoucher implements Serializable {

    @TableId(value = "voucher_id", type = IdType.INPUT)
    private Long voucher_id;

    private Integer stock;

    private Date create_time;

    private Date begin_time;

    private Date end_time;

    private Date update_time;

    private static final long serialVersionUID = 1L;
}