package com.delicoffee.deli.once;

import com.alibaba.excel.EasyExcel;
import com.delicoffee.deli.model.entity.DeliProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入excel
 */
@Slf4j
public class ImportDeliProducts {

    /**
     * 读取方法
     * @param args
     */
    public static void main(String[] args) {
        // 导入数据
        String fileName = "testExcel.xlsx"; // TODO 更改成绝对路径，文件放在resource下
        List<DeliProduct> userInfoList =  EasyExcel.read(fileName).head(DeliProduct.class).sheet().doReadSync();
        System.out.println("总数 = "+userInfoList.size());
        // 用户数据判重，使用map
        Map<String, List<DeliProduct>> listMap =
                userInfoList.stream().
                        filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername())).
                        collect(Collectors.groupingBy(DeliProduct::getUsername));
        for (Map.Entry<String, List<DeliProduct>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1){
                System.out.println("username = " + stringListEntry.getKey());
            }
        }
        System.out.println("不重复的昵称数 = "+listMap.keySet().size());
    }

    private static void synchronousRead(String fileName){


    }

}
