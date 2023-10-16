package com.delicoffee.deli.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.model.entity.DeliSeckillVoucher;
import com.delicoffee.deli.model.entity.DeliVoucherOrder;
import com.delicoffee.deli.service.DeliSeckillVoucherService;
import com.delicoffee.deli.service.DeliVoucherOrderService;
import com.delicoffee.deli.mapper.DeliVoucherOrderMapper;
import com.delicoffee.deli.service.DeliVoucherService;
import com.delicoffee.deli.util.RedisIdWorker;
import com.delicoffee.deli.util.RedisLock;
import com.delicoffee.deli.util.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @author fengxiaoha
* @description 针对表【deli_voucher_order】的数据库操作Service实现
* @createDate 2023-09-07 10:56:54
*/
// TODO VoucherOrderHandler是应用加载之后不断监听消息队列，取消注释来开启
@Service
public class DeliVoucherOrderServiceImpl extends ServiceImpl<DeliVoucherOrderMapper, DeliVoucherOrder>
    implements DeliVoucherOrderService{

    @Autowired
    private DeliSeckillVoucherService seckillVoucherService;

    @Autowired
    DeliVoucherOrderService voucherOrderService;

    @Autowired
    RedisIdWorker redisIdWorker;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    Logger logger = LoggerFactory.getLogger(DeliVoucherOrderServiceImpl.class);

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    // 提前加载lua脚本
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        // 设置导入资源文件的路径，默认从classpath下找，/resource
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    //private BlockingQueue<DeliVoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);
//    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

//    @PostConstruct
//    private void init(){
//        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
//    }
//    private class VoucherOrderHandler implements Runnable{
//        String queueName = "stream.orders";
//        @Override
//        public void run() {
//            while(true){
//                try {
//                    // 1 获取消息队列中的订单信息
//                    // redis中创建的消息队列 XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS stream.orders >
//                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                            Consumer.from("g1", "c1"),
//                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
//                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
//                    );
//                    // 2 判断消息获取是否成功
//                    if (list == null || list.isEmpty()){
//                        // 2。1如果获取失败，说明没有信息，继续下一次循环
//                        continue;
//                    }
//                    // 3 解析消息
//                    MapRecord<String, Object, Object> record = list.get(0);
//                    Map<Object, Object> values = record.getValue();
//                    DeliVoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new DeliVoucherOrder(), false);
//                    // 4 如果获取成功，可以下单
//                    handleVoucherOrder(voucherOrder);
//                    // 5 ACK确认 SACK stream.orders g1 id
//                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
//
//                } catch (Exception e) {
//                    // 获取消息是出现异常，则消息会进到pending list
//                    logger.error("处理订单异常",e);
//                    handlePendingList();
//                }
//            }
//        }
//        private void handlePendingList(){
//            while(true){
//                try {
//                    // 1 获取pending list中的订单信息
//                    // redis中创建的消息队列 XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS stream.orders 0
//                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                            Consumer.from("g1", "c1"),
//                            StreamReadOptions.empty().count(1),
//                            StreamOffset.create(queueName, ReadOffset.from("0"))
//                    );
//                    // 2 判断消息获取是否成功
//                    if (list == null || list.isEmpty()){
//                        // 2。1如果获取失败，说明没有信息，结束循环
//                        break;
//                    }
//                    // 3 解析消息
//                    MapRecord<String, Object, Object> record = list.get(0);
//                    Map<Object, Object> values = record.getValue();
//                    DeliVoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new DeliVoucherOrder(), false);
//                    // 4 如果获取成功，可以下单
//                    handleVoucherOrder(voucherOrder);
//                    // 5 ACK确认 SACK stream.orders g1 id
//                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
//
//                } catch (Exception e) {
//                    // 获取消息是出现异常，则消息会进到pending list
//                    logger.error("处理pending-list异常",e);
//                    try {
//                        // 休眠一会，处理异常不要太频繁
//                        Thread.sleep(20);
//                    } catch (InterruptedException interruptedException) {
//                        interruptedException.printStackTrace();
//                    }
//                }
//            }
//        }
//    }


    // 阻塞队列版
    // 增加注释，使得目标类创建完马上创建该类（阻塞队列），因为用户随时会创建订单
//    @PostConstruct
//    private void init(){
//        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
//    }
//    private class VoucherOrderHandler implements Runnable{
//        @Override
//        public void run() {
//            while(true){
//                try {
//                    // 1 获取队列中的订单信息
//                    DeliVoucherOrder voucherOrder = orderTasks.take();
//                    // 2 创建订单
//                    handleVoucherOrder(voucherOrder);
//                } catch (Exception e) {
//                    logger.error("处理订单异常",e);
//                }
//
//            }
//        }
//    }

    private void handleVoucherOrder(DeliVoucherOrder voucherOrder){
        Integer userId = voucherOrder.getUser_id();
        // 创建锁对象
        RLock lock = redissonClient.getLock("lock:order:"+userId);
        boolean isLock = lock.tryLock();

        if (!isLock){
            logger.error("不允许重复下单");
            return;
        }
        try{
            proxy.createVoucherOrder(voucherOrder);
        }finally {
            lock.unlock();
        }
    }

    private DeliVoucherOrderService proxy;

    @Override
    public Long seckillVoucher(Long voucherId){
        // 获取用户
        Integer userId = UserHolder.getUser().getId();
        // 获取订单id
        long orderId = redisIdWorker.nextId("order");
        // 1。执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId));
        // 2。判断结果是否为0
        int r = result.intValue();
        if (r != 0){
            // 2。1 不为0，没有购买资格
            return result;
        }
        // 需要在外层函数获取代理对象，保存到常量，让子线程去获取并创建事务
        proxy = (DeliVoucherOrderService)AopContext.currentProxy();
        // 3。返回订单id
        return orderId;
    }

//    @Override
//    public Long seckillVoucher(Long voucherId){
//        // 获取用户
//        Integer userId = UserHolder.getUser().getId();
//        // 1。执行lua脚本
//        Long result = stringRedisTemplate.execute(
//                SECKILL_SCRIPT,
//                Collections.emptyList(),
//                voucherId.toString(), userId.toString());
//        // 2。判断结果是否为0
//        int r = result.intValue();
//        if (r != 0){
//            // 2。1 不为0，没有购买资格
//            return result;
//        }
//        // 2。2 为0，有购买资格，把下单信息保存到阻塞队列
//        DeliVoucherOrder voucherOrder = new DeliVoucherOrder();
//        long orderId = redisIdWorker.nextId("order");
//        voucherOrder.setId(orderId);
//        voucherOrder.setUser_id(userId);
//        voucherOrder.setVoucher_id(voucherId);
//        voucherOrder.setCreate_time(new Date());
//        voucherOrder.setPay_time(new Date());
//        voucherOrder.setUse_time(new Date());
//        voucherOrder.setRefund_time(new Date());
//
//        orderTasks.add(voucherOrder);
//        // 需要在外层函数获取代理对象，保存到常量，让子线程去获取并创建事务
//        proxy = (DeliVoucherOrderService)AopContext.currentProxy();
//        // 3。返回订单id
//        return orderId;
//    }

//    @Override
//    public Long seckillVoucher(Long voucherId) {
//        // 1.查询优惠券
//        DeliSeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//
//        // 2.3.判断秒杀是否开始 或 结束
////        SimpleDateFormat sdf = new SimpleDateFormat();
////        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
//        // TODO datetime 格式有问题，存进有值但获取为null，还未解决
////        if (voucher.getBegin_time().after(new Date()) || voucher.getEnd_time().before(new Date())){
////            return null;
////        }
//
//        // 4.判断库存是否充足
//        if (voucher.getStock() < 1){
//            return null;
//        }
//
//        // 5.一人一单
//        // 获取用户id
//        Integer userId = UserHolder.getUser().getId();
//
//        // 使用lua脚本调用分布式锁
//        //RedisLock lock = new RedisLock("order:"+userId, stringRedisTemplate);
////        boolean isLock = lock.tryLock(1200);
//        // 使用Redisson创建的锁
//        RLock lock = redissonClient.getLock("lock:order:"+userId);
//        boolean isLock = lock.tryLock();
//
//        if (!isLock){
//            return null;
//        }
//        try{
//            DeliVoucherOrderService proxy = (DeliVoucherOrderService)AopContext.currentProxy();
//            return proxy.createVoucherOrder(userId, voucherId);
//        }finally {
//            lock.unlock();
//        }
//        // 使用悲观锁
////        synchronized (userId.toString().intern()) {
////            // 获取代理对象（事务）
////            DeliVoucherOrderService proxy = (DeliVoucherOrderService)AopContext.currentProxy();
////            return proxy.createVoucherOrder(userId, voucherId);
////        }
//    }

    @Transactional
    public void createVoucherOrder(DeliVoucherOrder voucherOrder) {
        // 5.一人一单
        // 获取用户id
        Integer userId = voucherOrder.getUser_id();
        // 查询订单
        long count = query().eq("user_id", userId).eq("voucher_id", voucherOrder.getVoucher_id()).count();
        if (count > 0) {
            // 该用户已经购买过了
            logger.error("用户不允许重复购买");
            return;
        }

        // 6.扣减库存
        // 乐观锁：在扣减库存时先做库存判断
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock-1")
                .eq("voucher_id", voucherOrder.getVoucher_id()).gt("stock", 0) // where id=? and stock=?
                .update();

        if (!success) {
            // 扣减失败
            logger.error("库存不足");
            return;
        }
        // 7.保存订单至数据库
        save(voucherOrder);
    }
}




