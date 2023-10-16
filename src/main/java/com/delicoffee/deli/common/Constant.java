package com.delicoffee.deli.common;

import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import com.google.common.collect.Sets;


/**
 * 常量值
 */
@Component
public class Constant {

    public static final String DELI_USER = "deli_user";
    public static final String SALT = "weiongdskl832?/";

    // 用于对token加解密
    public static final String JWT_KEY = "deli_coffee"; //有key才可以对JWT加解密
    public static final String JWT_TOKEN = "jwt_token";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_ROLE = "user_role";
    public static final Long EXPIRE_TIME = 60 * 1000 * 60 * 24 * 1000L; //单位毫秒 -> 现在设置成1000天

    // 商品查询返回结果的排序
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    // 与缓存相关的
    public static final Long CACHE_NULL_TTL = 100L;
    public static final Long CACHE_CATEGORY_TTL = 30L;
    public static final String CACHE_CATEGORY_KEY = "cache:category:";
    public static final String SECKILL_STOCK_KEY = "seckill:stock:";

    // 互斥锁
    public static final String LOCK_CATEGORY_KEY = "lock:category:";
    public static final Long LOCK_CATEGORY_TTL = 10L;

    // 商品在购物车的状态
    public interface Cart{
        int UN_CHECKED = 0; //商品未选中
        int CHECKED = 1; //商品选中
    }

    // 商品销售状态
    public interface SaleStatus{
        int NOT_SALE = 0; //商品下架状态
        int SALE = 1; //商品上架状态
    }

    public static String FILE_UPLOAD_DIR;
    // 静态资源无法被直接获取，需要写set()获取
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    // 订单状态
    public enum OrderStatusEnum{
        CANCELED(0, "用户已取消"),
        NOT_PAID(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");

        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public static OrderStatusEnum codeOf (int code){
            for (OrderStatusEnum orderStatusEnum: values()){
                if (orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.NO_ENUM);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

    }

}
