package com.delicoffee.deli.util;

import com.alibaba.excel.EasyExcel;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.once.DeliProductInfo;
import com.delicoffee.deli.once.TableListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入excel
 */

@Deprecated
@Slf4j
public class ImportDeliProducts {

    // 方法1：使用监听器读取数据
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, DeliProductInfo.class, new TableListener()).sheet().doRead();
    }
    // 方法2：开启同步读，不使用监听器
    public static List<DeliProductInfo> synchronousRead(String fileName){
        List<DeliProductInfo> productList =  EasyExcel.read(fileName).head(DeliProductInfo.class).sheet().doReadSync();
        System.out.println("总数 = "+productList.size());
        return productList;
    }

}
