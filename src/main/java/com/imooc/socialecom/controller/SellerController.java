package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Seller;
import com.imooc.socialecom.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Seller seller) {
        return JsonReturnType.createType(sellerService.save(seller));
    }
}
