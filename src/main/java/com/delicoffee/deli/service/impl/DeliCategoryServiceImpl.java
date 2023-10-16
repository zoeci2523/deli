package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.model.entity.DeliCategory;
import com.delicoffee.deli.service.DeliCategoryService;
import com.delicoffee.deli.mapper.DeliCategoryMapper;
import com.delicoffee.deli.util.CacheClient;
import com.delicoffee.deli.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author fengxiaoha
* @description 针对表【deli_category(商品分类)】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliCategoryServiceImpl extends ServiceImpl<DeliCategoryMapper, DeliCategory>
    implements DeliCategoryService{

    @Autowired
    DeliCategoryMapper categoryMapper;

    @Resource
    private CacheClient cacheClient;

    @Override
    public List<CategoryVO> listCategory(Integer parentId){
        List<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList, parentId);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId){
        // 递归获取所有子类别，并组合成一个"目录树"

        // TODO 选择商品品类数据加载方式
        // 取消注释即使用redis缓存，并设置空值解决缓存穿透问题
//        List<DeliCategory> categoryList = cacheClient.queryWithPassThrough(Constant.CACHE_CATEGORY_KEY, parentId,
//                List.class, id -> categoryMapper.selectCategoriesByParentId(id), Constant.CACHE_CATEGORY_TTL, TimeUnit.SECONDS);

        // 取消注释即使用redis缓存，并逻辑过期解决缓存击穿问题
//        List<DeliCategory> categoryList = cacheClient.queryWithLogicalExpire(Constant.CACHE_CATEGORY_KEY, parentId,
//                List.class, id -> categoryMapper.selectCategoriesByParentId(id), Constant.CACHE_CATEGORY_TTL, TimeUnit.SECONDS);

        // 取消注释即直接从数据库取出，不使用缓存
        List<DeliCategory> categoryList = categoryMapper.selectCategoriesByParentId(parentId);

        if (!CollectionUtils.isEmpty(categoryList)){
            for (int i = 0; i < categoryList.size(); i++){
                DeliCategory category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChild_category(), categoryVO.getId());
            }
        }
    }

}




