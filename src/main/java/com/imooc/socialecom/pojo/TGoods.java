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
 * 商品表
 * </p>
 *
 * @author socialecom
 * @since 2022-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TGoods对象", description="商品表")
public class TGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "商品类型 1-免费课 2-实战课 3-体系课")
    private Integer type;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品库存，-1代表没有库存限制")
    private Long stock;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
