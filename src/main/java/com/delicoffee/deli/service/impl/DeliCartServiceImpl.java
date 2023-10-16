package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.filter.UserFilter;
import com.delicoffee.deli.mapper.DeliProductMapper;
import com.delicoffee.deli.model.entity.DeliCart;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.service.DeliCartService;
import com.delicoffee.deli.mapper.DeliCartMapper;
import com.delicoffee.deli.vo.CartVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_cart(购物车)】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliCartServiceImpl extends ServiceImpl<DeliCartMapper, DeliCart>
    implements DeliCartService{

    @Resource
    DeliCartMapper cartMapper;

    @Resource
    DeliProductMapper productMapper;


    @Override
    public List<CartVO> listAll(){
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOList = cartMapper.selectList(userId);
        // 计算每个商品的总价
        for (int i=0; i<cartVOList.size(); i++){
            CartVO current = cartVOList.get(i);
            current.setTotal_price(current.getPrice() * current.getQuantity());
        }
        return cartVOList;
    }

    @Override
    public List<CartVO> add(Integer productId, Integer count){
        // 查看商品状态是否可售且库存是否充足
        validProduct(productId, count);

        Integer userId = UserFilter.currentUser.getId();
        // 查看该商品是否已经在购物车
        DeliCart cart = cartMapper.selectCartByUserIdAndProductId(productId, userId);
        if (cart == null){
            // 新建购物车
            cart = new DeliCart();
            cart.setProduct_id(productId);
            cart.setUser_id(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insert(cart);
        }else {
            // 应该将商品整个更新，重新插入并渲染，不要只更新数量
            count += cart.getQuantity();
            DeliCart cart1 = new DeliCart();
            cart1.setId(cart.getId());
            cart1.setProduct_id(cart.getProduct_id());
            cart1.setUser_id(cart.getUser_id());
            cart1.setQuantity(count);
            cart1.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateById(cart1);
        }
        return this.listAll();
    }

    @Override
    public List<CartVO> update(Integer productId, Integer count){
        validProduct(productId, count);

        Integer userId = UserFilter.currentUser.getId();
        // 如果商品不在购物车里，无法更新
        DeliCart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.UPDATE_FAILED);
        }else {
            // 应该将商品整个更新，重新插入并渲染，不要只更新数量
            DeliCart cart1 = new DeliCart();
            cart1.setId(cart.getId());
            cart1.setProduct_id(cart.getProduct_id());
            cart1.setUser_id(cart.getUser_id());
            cart1.setQuantity(count);
            cart1.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateById(cart1);
        }
        return this.listAll();
    }

    @Override
    public List<CartVO> delete(Integer productId){
        Integer userId = UserFilter.currentUser.getId();
        DeliCart cart = cartMapper.selectCartByUserIdAndProductId(productId, userId);
        if (cart == null){
            // 商品之前不在购物车里，无法删除
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.DELETE_FAILED);
        }else {
            cartMapper.deleteById(cart);
        }
        return this.listAll();
    }

    @Override
    public List<CartVO> selectOrNot(Integer productId, Integer selected){
        Integer userId = UserFilter.currentUser.getId();
        DeliCart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.UPDATE_FAILED);
        }else {
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.listAll();
    }

    @Override
    public List<CartVO> selectAllOrNot(Integer selected){
        Integer userId = UserFilter.currentUser.getId();
        cartMapper.selectOrNot(userId, null, selected);
        return this.listAll();
    }

    private void validProduct(Integer productId, Integer count){
        DeliProduct product = productMapper.selectByPrimaryKey(productId);

        // 查看商品状态是否可售
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_SALE);
        }
        // 查看商品库存是否充足
        if (count > product.getStock()){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_ENOUGH);
        }
    }


}




