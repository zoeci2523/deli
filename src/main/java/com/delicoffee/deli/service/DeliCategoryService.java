package com.delicoffee.deli.service;

import com.delicoffee.deli.model.entity.DeliCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicoffee.deli.vo.CategoryVO;

import java.util.List;

/**
* @author fengxiaoha
* @description 针对表【deli_category(商品分类)】的数据库操作Service
* @createDate 2023-09-03 07:31:51
*/
public interface DeliCategoryService extends IService<DeliCategory> {

    List<CategoryVO> listCategory(Integer parentId);
}
