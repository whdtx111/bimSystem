package org.springblade.modules.sp.entity;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_lib")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "属性字典对象", description = "属性字典实体表")
public class Library extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("units")
    private String units;
    @TableField("version")
    private String version;
    @TableField("category")
    private String category;
    //datacategory
    @TableField("parameter_type")
    private String parameterType;
    @TableField("data_type")
    private String dataType;
    @TableField("data_type_en")
    private String dataTypeEn;
    @TableField("parameters")
    private String parameters;
    @TableField("detail")
    private String detail;
    @TableField("type")
    private String type;
    @TableField("lod")
    private String lod;
    @TableField("revit_parameters")
    private String revitParameters;
    @TableField("revit_parameters_en")
    private String revitParametersEn;
    @TableField("tag")
    private String[] tag;

    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;
    @TableField("status")
    private Integer status;

    //    private RevitParameter revitParameter;
    @TableField("guid")
    private String guid;
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
    @TableField("source")
    private String source;

    public Library( String name, String units, String version, String category, String parameterType, String dataType, String dataTypeEn, String parameters, String detail, String type, String lod, String revitParameters, String revitParametersEn, String[] tag, String modifyUser, Date modifyTime, Integer status, String guid, String datacategory, String group, String visible, String description, String usermodifiable, String hidewhennovalue,String source) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.units = units;
        this.version = version;
        this.category = category;
        this.parameterType = parameterType;
        this.dataType = dataType;
        this.dataTypeEn = dataTypeEn;
        this.parameters = parameters;
        this.detail = detail;
        this.type = type;
        this.lod = lod;
        this.revitParameters = revitParameters;
        this.revitParametersEn = revitParametersEn;
        this.tag = tag;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = 0;
        this.guid = guid;
        this.datacategory = datacategory;
        this.group = group;
        this.visible = visible;
        this.description = description;
        this.usermodifiable = usermodifiable;
        this.hidewhennovalue = hidewhennovalue;
        this.source = source;
    }

        public Library(){
        this.id = UUID.randomUUID().toString();
        this.modifyTime =  new Date();
        this.status = 0;
    }
}
