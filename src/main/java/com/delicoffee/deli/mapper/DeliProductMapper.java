package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicoffee.deli.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_product(商品表)】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliProduct
*/

@Mapper
public interface DeliProductMapper extends BaseMapper<DeliProduct> {

    DeliProduct selectByPrimaryKey(Integer id);

    List<DeliProduct> selectList(@Param("query")ProductListQuery productListQuery);
}




