package com.imooc.socialecom.service.impl;

import com.imooc.socialecom.common.Constants;
import com.imooc.socialecom.demo.CacheDemo;
import com.imooc.socialecom.pojo.TGoods;
import com.imooc.socialecom.mapper.TGoodsMapper;
import com.imooc.socialecom.service.TGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-27
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements TGoodsService {

    @Autowired
    private TGoodsMapper tGoodsMapper;

    @Override
    @CacheDemo(cacheName = Constants.GOODS_CACHE_KEY, key = "#id")
    public TGoods findById(Long id) {
        return tGoodsMapper.selectById(id);
    }

    @Override
    @CacheDemo(cacheName = Constants.GOODS_CACHE_KEY_SAVE, key = "#tGoods")
    public TGoods saveDemo(TGoods tGoods) {
        tGoodsMapper.insert(tGoods);
        return tGoods;
    }


}
