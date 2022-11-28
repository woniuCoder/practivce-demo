package com.imooc.socialecom.service;

import com.imooc.socialecom.pojo.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
public interface StockService extends IService<Stock> {

    Stock increaseStock(Integer skuId, Integer shopId, Integer stock);

    boolean decreaseStock(Long skuId, Long shopId, Integer stock);

    boolean tradeRollbackStock(Long skuId, Long shopId, Integer stock);
}
