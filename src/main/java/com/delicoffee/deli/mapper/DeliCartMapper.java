package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicoffee.deli.vo.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_cart(购物车)】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliCart
*/
@Mapper
public interface DeliCartMapper extends BaseMapper<DeliCart> {
    List<CartVO> selectList(@Param("userId") Integer userId);

    DeliCart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId")Integer productId);

    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId")Integer productId, @Param("selected")Integer selected);
}




