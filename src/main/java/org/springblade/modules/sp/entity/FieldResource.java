package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_field_resource")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "字段资源实体", description = "字段资源实体")
public class FieldResource extends BaseEntity {

    private String id;

    private String code;

    private String tag;

    private String name;

    private String dataType;

    private String unit;

    private String type;

    private String value;

    private Integer required;

    private String modifyUser;

    private Date modifyTime;

    public FieldResource() {
        this.id = UUID.randomUUID().toString();
        this.modifyTime = new Date();
    }
    public FieldResource(String code, String tag, String name, String dataType, String unit, String type, String value, Integer required, String modifyUser, Date modifyTime) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.tag = tag;
        this.name = name;
        this.dataType = dataType;
        this.unit = unit;
        this.type = type;
        this.value = value;
        this.required = required;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
    }
}
