package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.AttributeName;
import com.imooc.socialecom.pojo.AttributeValue;
import com.imooc.socialecom.service.AttributeNameService;
import com.imooc.socialecom.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 可以使用的商品 前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/attribute-value")
public class AttributeValueController {
    @Autowired
    private AttributeValueService attributeValueService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody AttributeValue attributeValue) {
        return JsonReturnType.createType(attributeValueService.save(attributeValue));
    }
}
