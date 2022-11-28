package com.imooc.socialecom.pojo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author socialecom
 * @since 2022-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Trade对象", description = "")
public class Trade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Long skuId;

    private Long shopId;

    private Integer stockCount;

    private BigDecimal singlePrice;

    private BigDecimal totalPrice;

    private Long userId;

    private Integer status;

    //支付预下单
    private String wechatTradeId;

}
