package com.delicoffee.deli;

import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.mapper.DeliProductMapper;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.model.entity.DeliSeckillVoucher;
import com.delicoffee.deli.model.entity.DeliUser;
import com.delicoffee.deli.model.entity.DeliVoucher;
import com.delicoffee.deli.service.DeliSeckillVoucherService;
import com.delicoffee.deli.util.CacheClient;
import com.delicoffee.deli.util.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DeliApplicationTests {

    @Resource
    private CacheClient cacheClient;
    @Resource
    private DeliProductMapper productMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private DeliSeckillVoucherService seckillVoucherService;

//    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testVoucherOrder(){
        long id = redisIdWorker.nextId("order");
        System.out.println(id);
    }

    @Test
    void testVoucherDate(){
        Long voucherId = 1699680173392347137L;
        DeliSeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//        if (voucher.getBegin_time().after(new Date()) || voucher.getEnd_time().before(new Date())){
//            System.out.println("Voucher not available");
//        }

        System.out.println(voucher.getBegin_time());

        System.out.println("Voucher is used!");
    }

    // 500个线程每个生成100个id，提交到500个线程池里
//    @Test
//    void testIdWorker() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(300);
//        Runnable task = () ->{
//            for (int i = 0; i < 100; i++) {
//                long id = redisIdWorker.nextId("order");
//                System.out.println("id = "+id);
//            }
//            latch.countDown();
//        };
//
//        long begin = System.currentTimeMillis();
//        for (int i = 0; i < 300; i++) {
//            es.submit(task);
//        }
//
//        latch.await();
//        long end = System.currentTimeMillis();
//        System.out.println("time: "+(end-begin));
//    }

    @Test
    void testSave2Redis(){
        DeliProduct product = productMapper.selectByPrimaryKey(11);
        cacheClient.set(Constant.CACHE_CATEGORY_KEY, product, Constant.CACHE_CATEGORY_TTL, TimeUnit.SECONDS);
    }

    @Test
    void testSave2RedisWithLogicalExpire(){
        DeliProduct product = productMapper.selectByPrimaryKey(10);
        cacheClient.setWithLogicalExpire(Constant.CACHE_CATEGORY_KEY, product, Constant.CACHE_CATEGORY_TTL, TimeUnit.SECONDS);
    }

    @Test
    void testQueryWithPassThrough(){
        DeliProduct product = cacheClient.queryWithPassThrough(Constant.CACHE_CATEGORY_KEY, 100,
                DeliProduct.class, id1 -> productMapper.selectByPrimaryKey(id1), 300L, TimeUnit.SECONDS);
        if(product == null){
            System.out.println("空空如也...");
        }else{
            System.out.println(product.toString());
        }
    }

    @Test
    void testSetNull(){
        stringRedisTemplate.opsForValue().set("test1", "", Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
        String testKey = stringRedisTemplate.opsForValue().get("test");
        System.out.println("Get the key "+testKey);
    }


}
