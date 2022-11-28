package com.imooc.socialecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.socialecom.pojo.Item;
import com.imooc.socialecom.pojo.Sku;
import com.imooc.socialecom.pojo.Trade;
import com.imooc.socialecom.mapper.TradeMapper;
import com.imooc.socialecom.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Service
@SuppressWarnings("all")
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockService stockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Trade createTrade(Trade trade) {
        Sku sku = skuService.getById(trade.getSkuId());
        if (sku == null) {
            return null;
        }
        if (sku.getPrice().compareTo(trade.getSinglePrice()) != 0) {
            return null;
        }
        boolean decreaseStock = stockService.decreaseStock(trade.getSkuId(), trade.getShopId(), trade.getStockCount());
        if (!decreaseStock) {
            throw new RuntimeException("扣件库存失败");
        }
        trade.setStatus(1);
        save(trade);

        //进行三方支付
        String weChatTradeId = UUID.randomUUID().toString();
        trade.setWechatTradeId(weChatTradeId);
        updateById(trade);
        return trade;
    }

    @Override
    @Transactional
    public Trade pay(Trade trade) {
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("wechat_trade_id", trade.getWechatTradeId());
        queryWrapper.last("for updata");
        trade = getBaseMapper().selectOne(queryWrapper);
        if (trade == null) {
            return null;
        }
        //支付成功
        if (trade.getStatus().intValue() == 2) {
            return trade;
        }

        if (trade.getStatus().intValue() == 3) {
            return exceptionPay(trade, 1);
        }
        trade.setStatus(2);
        updateById(trade);
        return trade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Trade cancel(Trade trade) {
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", trade.getId());
        queryWrapper.last("for update");
        trade = getBaseMapper().selectOne(queryWrapper);
        if (trade == null) {
            return null;
        }
        //取消
        if (trade.getStatus() == 3) {
            return trade;
        }
        //已支付
        if (trade.getStatus() == 2) {
            return null;
        }
        boolean isSuccess = stockService.tradeRollbackStock(trade.getSkuId(), trade.getShopId(), trade.getStockCount());
        if (!isSuccess) {
            throw new RuntimeException("库存回补失败");
        }
        trade.setStatus(3);
        updateById(trade);
        return trade;
    }

    private Trade exceptionPay(Trade trade, int type) {
        if (type == 1) {
            //优先取消
            boolean refund = refund(trade.getWechatTradeId());
            if (refund) {
                return trade;
            }
            return null;
        } else {
            //优先支付
            boolean decreaseSuccess = stockService.decreaseStock(trade.getSkuId(), trade.getShopId(), trade.getStockCount());
            if (!decreaseSuccess) {
                if (refund(trade.getWechatTradeId())) {
                    return trade;
                } else {
                    return null;
                }
            } else {
                trade.setStatus(2);
                updateById(trade);
                return trade;
            }
        }
    }

    private boolean refund(String wechatTradeId) {
        return true;
    }
}
