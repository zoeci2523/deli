package com.delicoffee.deli.mapper;

import com.delicoffee.deli.model.entity.DeliCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_category(商品分类)】的数据库操作Mapper
* @createDate 2023-09-03 07:31:51
* @Entity com.delicoffee.deli.model.entity.DeliCategory
*/

@Mapper
public interface DeliCategoryMapper extends BaseMapper<DeliCategory> {

    List<DeliCategory> selectCategoriesByParentId(Integer parentId);
}




