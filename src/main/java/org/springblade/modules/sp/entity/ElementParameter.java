package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_element_parameter")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ElementParameter", description = "ElementParameter")
public class ElementParameter extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @TableField("id")
    private String id;
    @TableField("pkey")
    private String pkey;
    @TableField("name")
    private String name;
    @TableField("units")
    private String units;
    @TableField("category")
    private String category;


    public ElementParameter(String pkey, String name, String units, String category) {
        this.id = UUID.randomUUID().toString();
        this.pkey = pkey;
        this.name = name;
        this.units = units;
        this.category = category;
    }

    public ElementParameter() {
        this.id = UUID.randomUUID().toString();
    }
}
