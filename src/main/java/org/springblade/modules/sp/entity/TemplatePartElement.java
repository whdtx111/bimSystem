package org.springblade.modules.sp.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@Slf4j
@TableName("sp_template_part_element")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TemplatePartElement", description = "TemplatePartElement")
public class TemplatePartElement extends BaseEntity {

    private String id;
    private String templateId;
    
    // 数据库中存储为字符串
    @JsonIgnore
    private String element;
    
    // 用于JSON序列化和反序列化
    @TableField(exist = false)
    private JSONObject[] elementArray;
    
    private Date createTime;

    public TemplatePartElement() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
    }

    public TemplatePartElement(String id, String templateId, JSONObject[] elementArray, Date createTime) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.templateId = templateId;
        this.setElementArray(elementArray);
        this.createTime = createTime != null ? createTime : new Date();
    }
    
    // 获取JSON数组
    @JsonProperty("element")
    public JSONObject[] getElementArray() {
        if (this.element == null || this.element.isEmpty()) {
            return new JSONObject[0];
        }
        try {
            return JSON.parseObject(this.element, JSONObject[].class);
        } catch (Exception e) {
            log.error("解析JSON数组失败", e);
            return new JSONObject[0];
        }
    }
    
    // 设置JSON数组，同时更新字符串表示
    @JsonProperty("element")
    public void setElementArray(JSONObject[] elementArray) {
        this.elementArray = elementArray;
        if (elementArray != null) {
            this.element = JSON.toJSONString(elementArray);
        } else {
            this.element = "[]";
        }
    }
}
