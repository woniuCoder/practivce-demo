package com.imooc.socialecom.pojo;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.ArrayList;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Item对象", description="商品")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String name;

    private Integer sellerId;

    private Long categoryId;

    private Long brandId;

    @TableField(exist = false)
    private String sellerName;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String brand;

    @TableField(exist = false)
    private List<Sku> skus = new ArrayList<>();
}
