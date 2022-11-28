package com.imooc.socialecom.controller;


import com.imooc.socialecom.pojo.TGoods;
import com.imooc.socialecom.service.TGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-27
 */
@RestController
@RequestMapping("/t-goods")
public class TGoodsController {

    @Autowired
    private TGoodsService tGoodsService;

    @GetMapping("/findById")
    public TGoods findById(@RequestParam Long id) {
        return tGoodsService.findById(id);
    }

    @PostMapping("/save")
    public TGoods save(@RequestBody TGoods tGoods) {
        return tGoodsService.saveDemo(tGoods);
    }
}
