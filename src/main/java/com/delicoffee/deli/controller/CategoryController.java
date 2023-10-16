package com.delicoffee.deli.controller;

import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.service.DeliCategoryService;
import com.delicoffee.deli.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.java2d.pipe.AAShapePipe;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    DeliCategoryService categoryService;

    @PostMapping("category/list")
    @ResponseBody
    public ApiRestResponse listCategory(){
        List<CategoryVO> categoryVOList = categoryService.listCategory(0);
        return ApiRestResponse.success(categoryVOList);
    }
}
