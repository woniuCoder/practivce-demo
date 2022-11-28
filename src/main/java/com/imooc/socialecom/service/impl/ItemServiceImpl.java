package com.imooc.socialecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.socialecom.pojo.Item;
import com.imooc.socialecom.mapper.ItemMapper;
import com.imooc.socialecom.pojo.Sku;
import com.imooc.socialecom.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Service
@SuppressWarnings("all")
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    private final SkuService skuService;
    private final SkuAttributeInfoService skuAttributeInfoService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SellerService sellerService;

    @Autowired
    private RedisTemplate redisTemplate;

    public ItemServiceImpl(SkuService skuService, SkuAttributeInfoService skuAttributeInfoService,
                           BrandService brandService, CategoryService categoryService, SellerService sellerService) {
        this.skuService = skuService;
        this.skuAttributeInfoService = skuAttributeInfoService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.sellerService = sellerService;
    }

    @Override
    @Transactional
    public Sku createSku(Sku sku) {
        skuService.save(sku);

        sku.getSkuAttributeInfos().forEach(skuAttributeInfos -> {
            skuAttributeInfos.setSkuId(sku.getId());
            skuAttributeInfoService.save(skuAttributeInfos);
        });
        return sku;
    }

    @Override
//    @DS("slave1") 读写分离
    public Item getItem(Long id, Integer shopId) {
        Item item = getById(id);
        if (item == null) {
            return null;
        }
        item.setSellerName(sellerService.getById(item.getSellerId()).getSellerName());
        item.setBrand(brandService.getById(item.getBrandId()).getName());
        item.setCategoryName(categoryService.getById(item.getCategoryId()).getName());

        QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", item.getId());
        List<Sku> skus = skuService.listByShopId(queryWrapper, shopId);
        item.setSkus(skus);
        return item;
    }

    @Override
    public Item getItemByRedis(Long id, Integer shopId) {
        Item item = null;
        String itemRediskey = "itemId_" + id;
        String skuRedisKey = "itemId_" + id + "_skuList_shopId_" + shopId;

        item = (Item) redisTemplate.opsForValue().get(itemRediskey);
        if (item == null) {
            item = getById(id);
            if (item == null) {
                return null;
            }
        }
        item.setSellerName(sellerService.getById(item.getSellerId()).getSellerName());
        item.setBrand(brandService.getById(item.getBrandId()).getName());
        item.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
        redisTemplate.opsForValue().set(itemRediskey, item, 10, TimeUnit.MINUTES);

        //取sku
        List<Sku> skuList = (List<Sku>) redisTemplate.opsForValue().get(skuRedisKey);
        if (skuList == null) {
            QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", item.getId());
            skuList = skuService.listByShopId(queryWrapper, shopId);
            redisTemplate.opsForValue().set(skuRedisKey, skuList, 10, TimeUnit.MINUTES);
        }
        item.setSkus(skuList);
        return item;
    }

}
