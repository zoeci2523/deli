package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.model.entity.DeliSeckillVoucher;
import com.delicoffee.deli.model.entity.DeliVoucher;
import com.delicoffee.deli.service.DeliSeckillVoucherService;
import com.delicoffee.deli.service.DeliVoucherService;
import com.delicoffee.deli.mapper.DeliVoucherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher】的数据库操作Service实现
* @createDate 2023-09-07 10:56:54
*/
@Service
public class DeliVoucherServiceImpl extends ServiceImpl<DeliVoucherMapper, DeliVoucher>
    implements DeliVoucherService{

    @Resource
    private DeliSeckillVoucherService seckillVoucherService;

    @Autowired
    DeliVoucherMapper voucherMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public ApiRestResponse queryVoucherOfProduct(Long productId) {
        // 查询优惠券信息
        List<DeliVoucher> vouchers = voucherMapper.queryVoucherOfShop(productId);
        // 返回结果
        return ApiRestResponse.success(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(DeliVoucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        DeliSeckillVoucher seckillVoucher = new DeliSeckillVoucher();
        seckillVoucher.setVoucher_id(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBegin_time(voucher.getBegin_time());
        seckillVoucher.setEnd_time(voucher.getEnd_time());
        seckillVoucherService.save(seckillVoucher);
        // 保存秒杀库存到Redis，为了后续快速查询一人一单
        stringRedisTemplate.opsForValue().set(Constant.SECKILL_STOCK_KEY+voucher.getId(), voucher.getStock().toString());
    }

}




