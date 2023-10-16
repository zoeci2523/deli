package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.model.entity.DeliSeckillVoucher;
import com.delicoffee.deli.model.entity.DeliVoucherOrder;
import com.delicoffee.deli.service.DeliSeckillVoucherService;
import com.delicoffee.deli.mapper.DeliSeckillVoucherMapper;
import com.delicoffee.deli.service.DeliVoucherOrderService;
import com.delicoffee.deli.service.DeliVoucherService;
import com.delicoffee.deli.util.RedisIdWorker;
import com.delicoffee.deli.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
* @author fengxiaoha
* @description 针对表【deli_seckill_voucher】的数据库操作Service实现
* @createDate 2023-09-07 11:07:49
*/
@Service
public class DeliSeckillVoucherServiceImpl extends ServiceImpl<DeliSeckillVoucherMapper, DeliSeckillVoucher>
    implements DeliSeckillVoucherService{


}




