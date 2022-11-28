package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.AttributeName;
import com.imooc.socialecom.service.AttributeNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品属性库 前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/attribute-name")
public class AttributeNameController {

    @Autowired
    private AttributeNameService attributeNameService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody AttributeName attributeName) {
        return JsonReturnType.createType(attributeNameService.save(attributeName));
    }
}
