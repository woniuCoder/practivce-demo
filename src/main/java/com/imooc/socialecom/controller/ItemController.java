package com.imooc.socialecom.controller;


import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.pojo.Item;
import com.imooc.socialecom.pojo.Sku;
import com.imooc.socialecom.service.ItemService;
import com.imooc.socialecom.service.SkuService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final SkuService skuService;

    public ItemController(ItemService itemService, SkuService skuService) {
        this.itemService = itemService;
        this.skuService = skuService;
    }

    @PostMapping("/create")
    public JsonReturnType create(@RequestBody Item item) {
        return JsonReturnType.createType(itemService.save(item));
    }

    @PostMapping("/create-sku")
    public JsonReturnType createSku(@RequestBody Sku sku) {
        return JsonReturnType.createType(itemService.createSku(sku));
    }

    @GetMapping("/list")
    public JsonReturnType list(@RequestParam(value = "id") Long id, @RequestParam(value = "shopId") Integer shopId) {
        Item item = itemService.getItem(id, shopId);
        return JsonReturnType.createType(item);
    }

    @GetMapping("/listByRedis")
    public JsonReturnType listByRedis(@RequestParam(value = "id") Long id, @RequestParam(value = "shopId") Integer shopId) {
        Item item = itemService.getItemByRedis(id, shopId);
        return JsonReturnType.createType(item);
    }
}
