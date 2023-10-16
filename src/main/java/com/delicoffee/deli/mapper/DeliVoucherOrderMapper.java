package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliVoucherOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher_order】的数据库操作Mapper
* @createDate 2023-09-07 10:56:54
* @Entity com.delicoffee.deli.model.entity.DeliVoucherOrder
*/

@Mapper
public interface DeliVoucherOrderMapper extends BaseMapper<DeliVoucherOrder> {

}




