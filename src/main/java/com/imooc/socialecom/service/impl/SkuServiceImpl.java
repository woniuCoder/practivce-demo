package com.imooc.socialecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.socialecom.pojo.AttributeValue;
import com.imooc.socialecom.pojo.Sku;
import com.imooc.socialecom.mapper.SkuMapper;
import com.imooc.socialecom.pojo.Stock;
import com.imooc.socialecom.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private SkuAttributeInfoService attributeInfoService;
    @Autowired
    private AttributeNameService attributeNameService;
    @Autowired
    private AttributeValueService attributeValueService;
    @Autowired
    private StockService stockService;

    @Override
    public List<Sku> listByShopId(QueryWrapper<Sku> queryWrapper, Integer shopId) {
        List<Sku> objectList = super.list(queryWrapper);
        for (Sku sku : objectList) {
            QueryWrapper attributeQueryMapper = new QueryWrapper();
            attributeQueryMapper.eq("sku_id", sku.getId());
            sku.setSkuAttributeInfos(attributeInfoService.list(attributeQueryMapper));
            sku.getSkuAttributeInfos().forEach(attributeInfo -> {
                attributeInfo.setAttributeName(attributeNameService.getById(attributeInfo.getAttributeNameId()).getName());
                attributeInfo.setAttributeValue(attributeValueService.getById(attributeInfo.getAttributeValueId()).getName());
            });
            QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
            stockQueryWrapper.eq("sku_id", sku.getId());
            stockQueryWrapper.eq("shop_id", shopId);
            List<Stock> list = stockService.list(stockQueryWrapper);
            if (list.size() > 0) {
                sku.setStockCount(list.stream().mapToInt(Stock::getStockCount).sum());
            } else {
                sku.setStockCount(0);
            }
        }
        return objectList;
    }
}
