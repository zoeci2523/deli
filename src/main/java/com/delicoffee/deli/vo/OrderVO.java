package com.delicoffee.deli.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 返回前端的订单封装类;
 */

@Data
public class OrderVO implements Serializable {

    /**
     * 订单号（非主键id）
     */
    private String order_no;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 订单总价格
     */
    private Integer total_price;

    /**
     * 收货人姓名快照
     */
    private String receiver_name;

    /**
     * 收货人手机号快照
     */
    private String receiver_mobile;

    /**
     * 收货地址快照
     */
    private String receiver_address;

    /**
     * 订单状态: 0用户已取消，10未付款（初始状态），20已付款，30已发货，40交易完成
     */
    private Integer order_status;

    /**
     * 运费，默认为0
     */
    private Integer postage;

    /**
     * 支付类型,1-在线支付
     */
    private Integer payment_type;

    /**
     * 发货时间
     */
    private Date delivery_time;

    /**
     * 支付时间
     */
    private Date pay_time;

    /**
     * 交易完成时间
     */
    private Date end_time;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 订单状态名称
     */
    private String orderStatusName;

    /**
     * 订单商品信息
     */
    private List<OrderItemVO> order_itemVO_list;

    private static final long serialVersionUID = 1L;
}