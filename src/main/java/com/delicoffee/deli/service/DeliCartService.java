package com.delicoffee.deli.service;

import com.delicoffee.deli.model.entity.DeliCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicoffee.deli.vo.CartVO;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_cart(购物车)】的数据库操作Service
* @createDate 2023-09-03 07:31:51
*/
public interface DeliCartService extends IService<DeliCart> {

    List<CartVO> listAll();

    List<CartVO> add(Integer productId, Integer count);

    List<CartVO> update(Integer productId, Integer count);

    List<CartVO> delete(Integer productId);

    List<CartVO> selectOrNot(Integer productId, Integer selected);

    List<CartVO> selectAllOrNot(Integer selected);
}
