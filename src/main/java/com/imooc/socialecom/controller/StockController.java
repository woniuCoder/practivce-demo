package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Stock;
import com.imooc.socialecom.service.StockService;
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
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Stock stock) {
        return JsonReturnType.createType(stockService.increaseStock(stock.getSkuId(), stock.getShopId(), stock.getStockCount()));
    }
}
