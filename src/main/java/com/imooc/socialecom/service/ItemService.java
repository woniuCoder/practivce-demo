package com.imooc.socialecom.service;

import com.imooc.socialecom.pojo.Item;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.socialecom.pojo.Sku;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
public interface ItemService extends IService<Item> {

    Sku createSku(Sku sku);

    Item getItem(Long id, Integer shopId);

    Item getItemByRedis(Long id, Integer shopId);
}
