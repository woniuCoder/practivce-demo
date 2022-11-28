package com.imooc.socialecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.socialecom.pojo.Stock;
import com.imooc.socialecom.mapper.StockMapper;
import com.imooc.socialecom.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Override
    @Transactional
    public Stock increaseStock(Integer skuId, Integer shopId, Integer stock) {
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", stock);
        queryWrapper.eq("shop_id", shopId);
        Stock info = getBaseMapper().selectOne(queryWrapper);
        if (info == null) {
            try {
                info = new Stock();
                info.setStockCount(stock);
                info.setSkuId(stock);
                info.setShopId(stock);
                save(info);
            } catch (DuplicateKeyException duplicateKeyException) {
                updateStock(shopId, stock, skuId);
            }
        } else {
            updateStock(shopId, stock, skuId);
        }
        return info;
    }

    @Override
    public boolean decreaseStock(Long skuId, Long shopId, Integer stock) {
        boolean b = getBaseMapper().decreaseStock(skuId, shopId, stock);
        return b;
    }

    @Override
    public boolean tradeRollbackStock(Long skuId, Long shopId, Integer stock) {
        boolean b = getBaseMapper().tradeRollbackStock(skuId, shopId, stock);
        return b;
    }

    private void updateStock(Integer shopId, Integer stockCount, Integer skuId) {
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId);
        queryWrapper.eq("shop_id", shopId);
        queryWrapper.last("for update");
        Stock stock = getBaseMapper().selectOne(queryWrapper);
        stock.setStockCount(stock.getStockCount() + stockCount);
        updateById(stock);
    }


}
