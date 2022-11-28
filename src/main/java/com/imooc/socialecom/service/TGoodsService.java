package com.imooc.socialecom.service;

import com.imooc.socialecom.pojo.TGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-27
 */
public interface TGoodsService extends IService<TGoods> {

    TGoods findById(Long id);

    TGoods saveDemo(TGoods tGoods);
}
