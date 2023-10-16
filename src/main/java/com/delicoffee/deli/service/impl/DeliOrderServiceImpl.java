package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.filter.UserFilter;
import com.delicoffee.deli.mapper.DeliCartMapper;
import com.delicoffee.deli.mapper.DeliOrderItemMapper;
import com.delicoffee.deli.mapper.DeliProductMapper;
import com.delicoffee.deli.model.entity.DeliCart;
import com.delicoffee.deli.model.entity.DeliOrder;
import com.delicoffee.deli.model.entity.DeliOrderItem;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.model.request.CreateOrderReq;
import com.delicoffee.deli.service.DeliCartService;
import com.delicoffee.deli.service.DeliOrderService;
import com.delicoffee.deli.mapper.DeliOrderMapper;
import com.delicoffee.deli.util.OrderCodeFactory;
import com.delicoffee.deli.util.QRCodeGenerator;
import com.delicoffee.deli.vo.CartVO;
import com.delicoffee.deli.vo.OrderItemVO;
import com.delicoffee.deli.vo.OrderVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_order(订单表;)】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliOrderServiceImpl extends ServiceImpl<DeliOrderMapper, DeliOrder>
    implements DeliOrderService{

    @Resource
    DeliProductMapper productMapper;

    @Resource
    DeliCartMapper cartMapper;

    @Resource
    DeliCartService cartService;

    @Resource
    DeliOrderMapper orderMapper;

    @Resource
    DeliOrderItemMapper orderItemMapper;

    @Value("${file.upload.ip}")
    String ip;

    @Override
    public String create(CreateOrderReq createOrderReq){
        // 拿到用户id
        Integer userId = UserFilter.currentUser.getId();
        // 获得该用户的选中的购物车
        List<CartVO> cartVOList = cartService.listAll();
        List<CartVO> cartVOListTmp = new ArrayList<>();
        for (int i=0; i<cartVOList.size(); i++){
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)){
                cartVOListTmp.add(cartVO);
            }
        }
        cartVOList = cartVOListTmp;
        // 判空
        if (CollectionUtils.isEmpty(cartVOList)){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.CART_EMPTY);
        }
        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        //购物车对象转为订单item对象
        List<DeliOrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);

        //扣库存
        for (int i=0; i<orderItemList.size(); i++){
            DeliOrderItem orderItem = orderItemList.get(i);
            DeliProduct product = productMapper.selectByPrimaryKey(orderItem.getProduct_id());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0){
                throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
        }
        //把购物车中已勾选商品删除
        cleanCart(cartVOList);

        //生成新订单
        DeliOrder order = new DeliOrder();
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrder_no(orderNo);
        order.setUser_id(userId);
        order.setTotal_price(totalPrice(orderItemList));
        order.setReceiver_name(createOrderReq.getReceiver_name());
        order.setReceiver_mobile(createOrderReq.getReceiver_mobile());
        order.setReceiver_address(createOrderReq.getReceiver_address());
        order.setOrder_status(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPayment_type(1);

        //插入到order表
        orderMapper.insert(order);

        //循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); i++){
            DeliOrderItem orderItem = orderItemList.get(i);
            orderItem.setOrder_no(order.getOrder_no());
            orderItemMapper.insert(orderItem);
        }

        //返回订单号
        return orderNo;
    }

    @Override
    public OrderVO detail(String orderNo){
        DeliOrder order = orderMapper.selectByOrderNo(orderNo);
        // 如果订单不存在则报错
        if (order == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NO_ORDER);
        }
        // 查看订单所属人
        Integer userId = UserFilter.currentUser.getId();
        if (order.getUser_id() != userId){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_YOUR_ORDER);
        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize){
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum, pageSize);
        List<DeliOrder> orderList = orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void cancel(String orderNo){
        DeliOrder order = orderMapper.selectByOrderNo(orderNo);
        // 判断订单是否存在
        if (order == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NO_ORDER);
        }
        // 判断订单是否属于用户
        Integer userId = UserFilter.currentUser.getId();
        if (order.getUser_id() != userId){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_YOUR_ORDER);
        }
        //仅未付款时才允许取消
        if (order.getOrder_status().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrder_status(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEnd_time(new Date());
            orderMapper.updateById(order);
        }else{
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public String qrcode(String orderNo){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String address = ip +":" + request.getLocalPort();
        String payUrl = "http://"+address+"/pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350,Constant.FILE_UPLOAD_DIR+orderNo+".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://"+address+"/images/"+orderNo+".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo){
        DeliOrder order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NO_ORDER);
        }
        if (order.getOrder_status() == Constant.OrderStatusEnum.NOT_PAID.getCode()){
            order.setOrder_status(Constant.OrderStatusEnum.PAID.getCode());
            order.setPay_time(new Date());
            orderMapper.updateById(order);
        }else{
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList){
        // 检查商品是否可卖
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            DeliProduct product = productMapper.selectByPrimaryKey(cartVO.getProduct_id());
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
                throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_SALE);
            }
            if (cartVO.getQuantity() > product.getStock()){
                throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    private List<DeliOrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList){
        List<DeliOrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            DeliOrderItem orderItem = new DeliOrderItem();
            orderItem.setProduct_id(cartVO.getProduct_id());
            // 记录商品快照信息
            orderItem.setProduct_name(cartVO.getProduct_name());
            orderItem.setProduct_img(cartVO.getProduct_image());
            orderItem.setUnit_price(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotal_price(cartVO.getTotal_price());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void cleanCart(List<CartVO> cartVOList){
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            cartMapper.deleteById(cartVO.getId());
        }
    }

    private Integer totalPrice(List<DeliOrderItem> orderItemList){
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            totalPrice += orderItemList.get(i).getTotal_price();
        }
        return totalPrice;
    }

    private OrderVO getOrderVO(DeliOrder order){
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对应的orderItemVOList
        List<DeliOrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrder_no());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            DeliOrderItem orderItem = orderItemList.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrder_itemVO_list(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrder_status()).getValue());
        return orderVO;
    }

    private List<OrderVO> orderListToOrderVOList(List<DeliOrder> orderList){
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            OrderVO orderVO = getOrderVO(orderList.get(i));
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }
}




