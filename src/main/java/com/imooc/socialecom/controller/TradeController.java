package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Trade;
import com.imooc.socialecom.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Trade trade) {
        return JsonReturnType.createType(tradeService.createTrade(trade));
    }

    @PostMapping("/pay")
    public JsonReturnType pay(@RequestBody Trade trade) {
        trade = tradeService.pay(trade);
        if (trade != null) {
            return JsonReturnType.createErrorType("lose");
        }
        return JsonReturnType.createType("success");
    }

    @PostMapping("/cancel")
    public JsonReturnType cancel(@RequestBody Trade trade) {
        try {
            trade = tradeService.cancel(trade);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonReturnType.createType("取消失败");
        }
        if (trade == null) {
            return JsonReturnType.createType("取消失败");
        }
        return JsonReturnType.createType("取消成功");
    }

}
