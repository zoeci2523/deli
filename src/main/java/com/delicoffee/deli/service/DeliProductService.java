package com.delicoffee.deli.service;

import com.delicoffee.deli.model.entity.DeliProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicoffee.deli.model.request.ProductListReq;
import com.github.pagehelper.PageInfo;

/**
* @author fengxiaoha
* @description 针对表【deli_product(商品表)】的数据库操作Service
* @createDate 2023-09-03 07:31:51
*/
public interface DeliProductService extends IService<DeliProduct> {

    DeliProduct detail(Integer id);
    
    PageInfo list(ProductListReq productListReq);
}
