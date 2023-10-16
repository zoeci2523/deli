package com.delicoffee.deli.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成订单号工具类：使用时间+随机数
 */
public class OrderCodeFactory {
    private static String getDateTime(){
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
    private static int getRandom(Long n){
        Random random = new Random();
        // 生成固定位数 - 获取5位随机数
        return (int)(random.nextDouble() * (90000))+10000;
    }
    public static String getOrderCode(Long userId){
        return getDateTime() + getRandom(userId);
    }
}
