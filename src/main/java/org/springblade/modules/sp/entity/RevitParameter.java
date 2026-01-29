package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_revit_parameter")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "属性字典对象", description = "属性字典实体表")
public class RevitParameter extends BaseEntity {

    private static final long serialVersionUID = 1L;
//    @TableField("id")
//    private String id;
    @TableField("guid")
    private String guid;
    @TableField("name")
    private String name;
    @TableField("datatype")
    private String datatype;
    @TableField("datacategory")
    private String datacategory;
    @TableField("group")
    private String group;
    @TableField("visible")
    private String visible;
    @TableField("description")
    private String description;
    @TableField("usermodifiable")
    private String usermodifiable;
    @TableField("hidewhennovalue")
    private String hidewhennovalue;

    public RevitParameter(String guid, String name, String datatype, String datacategory, String group, String visible, String description, String usermodifiable, String hidewhennovalue) {
//        this.id = UUID.randomUUID().toString();
        this.guid = guid;
        this.name = name;
        this.datatype = datatype;
        this.datacategory = datacategory;
        this.group = group;
        this.visible = visible;
        this.description = description;
        this.usermodifiable = usermodifiable;
        this.hidewhennovalue = hidewhennovalue;
    }

}

