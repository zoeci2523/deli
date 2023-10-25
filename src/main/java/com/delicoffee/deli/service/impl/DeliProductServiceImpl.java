package com.delicoffee.deli.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.model.entity.DeliProduct;
import com.delicoffee.deli.model.query.ProductListQuery;
import com.delicoffee.deli.model.request.ProductListReq;
import com.delicoffee.deli.service.DeliCategoryService;
import com.delicoffee.deli.service.DeliProductService;
import com.delicoffee.deli.mapper.DeliProductMapper;
import com.delicoffee.deli.util.CacheClient;
import com.delicoffee.deli.vo.CategoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author fengxiaoha
* @description 针对表【deli_product(商品表)】的数据库操作Service实现
* @createDate 2023-09-03 07:31:51
*/
@Service
public class DeliProductServiceImpl extends ServiceImpl<DeliProductMapper, DeliProduct>
    implements DeliProductService{

    @Autowired
    DeliProductMapper productMapper;

    @Autowired
    DeliCategoryService categoryService;

    @Resource
    private CacheClient cacheClient;

    @Override
    public DeliProduct detail(Integer id){
        // TODO 取消注释，直接访问数据库
        DeliProduct product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        //批量查询，规定一个专门负责查询的Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())){
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理：需要返回target目录及其所有子目录
        if (productListReq.getCategoryId() != null){
            List<CategoryVO> categoryVOList = categoryService.listCategory(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        // 排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(), orderBy);
        }else {
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }

        // TODO 选择商品数据加载方式
        // 取消注释即使用redis缓存，并设置空值解决缓存穿透问题
//        List<DeliProduct> productList = cacheClient.queryWithPassThrough(Constant.CACHE_PRODUCT_KEY, productListQuery,
//                List.class, query -> productMapper.selectList(query), Constant.CACHE_PRODUCT_TTL, TimeUnit.SECONDS);

        // 取消注释即使用redis缓存，并逻辑过期解决缓存击穿问题
//        List<DeliProduct> productList = cacheClient.queryWithLogicalExpire(Constant.CACHE_PRODUCT_KEY, productListQuery,
//                List.class, query -> productMapper.selectList(query), Constant.CACHE_PRODUCT_TTL, TimeUnit.SECONDS);

        // 取消注释即直接从数据库取出，不使用缓存
        List<DeliProduct> productList = productMapper.selectList(productListQuery);

        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds){
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if(categoryVO != null){
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChild_category(), categoryIds);
            }
        }
    }

}




