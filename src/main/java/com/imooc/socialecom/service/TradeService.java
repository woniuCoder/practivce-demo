package com.imooc.socialecom.service;

import com.imooc.socialecom.pojo.Trade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
public interface TradeService extends IService<Trade> {

    Trade createTrade(Trade trade);

    Trade pay(Trade trade);

    Trade cancel(Trade trade);
}
