package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Shop;
import com.imooc.socialecom.service.ShopService;
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
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Shop shop) {
        return JsonReturnType.createType(shopService.save(shop));
    }

}
