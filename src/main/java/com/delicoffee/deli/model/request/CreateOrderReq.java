package com.delicoffee.deli.model.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 封装订单查询的请求类
 */
@Data
public class CreateOrderReq {

    /**
     * 收货人姓名快照
     */
    @NotNull
    private String receiver_name;

    /**
     * 收货人手机号快照
     */
    @NotNull
    private String receiver_mobile;

    /**
     * 收货地址快照
     */
    @NotNull
    private String receiver_address;

    /**
     * 运费，默认为0
     */
    private Integer postage;

    /**
     * 支付类型,1-在线支付
     */
    private Integer payment_type;

}
