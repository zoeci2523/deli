package com.delicoffee.deli.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicoffee.deli.model.entity.DeliVoucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher】的数据库操作Mapper
* @createDate 2023-09-07 10:56:54
* @Entity com.delicoffee.deli.model.entity.DeliVoucher
*/

@Mapper
public interface DeliVoucherMapper extends BaseMapper<DeliVoucher> {

    List<DeliVoucher> queryVoucherOfShop(@Param("productId") Long productId);

}




