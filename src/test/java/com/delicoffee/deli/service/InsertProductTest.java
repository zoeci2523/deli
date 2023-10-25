package com.delicoffee.deli.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.once.DeliProductInfo;
import com.delicoffee.deli.util.ImportDeliProducts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@Slf4j
public class InsertProductTest {

    @Resource
    DeliProductService productService;

    private ExecutorService executorService = new ThreadPoolExecutor(10, 100, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000));

    private String fileName = "/Users/fengxiaoha/Documents/code/delicoffee/excel/deli_product.xlsx";
    /**
     * 串行插入商品
     */
    @Test
    public void doInsertProducts(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //final int INSERT_NUM = 5000;

        //List<DeliProduct> productList = new ArrayList<>();
        EasyExcel.read(fileName, DeliProductInfo.class, new ReadListener<DeliProductInfo>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 1000;
            /**
             *临时存储
             */
            private List<DeliProduct> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(DeliProductInfo data, AnalysisContext context) {
                DeliProduct product = new DeliProduct();
                BeanUtils.copyProperties(data, product);
                product.setCreate_time(new Date());
                product.setUpdate_time(new Date());
                cachedDataList.add(product);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                productService.saveBatch(cachedDataList, BATCH_COUNT);
                log.info("存储数据库成功！");
            }
        }).sheet().doRead();

        stopWatch.stop();
        System.out.println("Total time used: "+stopWatch.getTotalTimeMillis());

    }

    /**
     * 并发批量插入商品
     */
//    @Test
    public void doConcurrencyInsertProducts(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int batchSize = 1000;
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<DeliProductInfo> productList = new ArrayList<>();
            while(true){
                DeliProductInfo product = new DeliProductInfo();
                ImportDeliProducts.readByListener(Constant.PRODUCT_IMPORT_FILE);
            }
        }

        stopWatch.stop();
        System.out.println("Total time used: "+stopWatch.getTotalTimeMillis());

    }
}
