package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Category;
import com.imooc.socialecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Category category) {
        return JsonReturnType.createType(categoryService.save(category));
    }

}
