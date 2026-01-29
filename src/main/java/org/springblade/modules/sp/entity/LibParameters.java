package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_lib_parameters")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分属性表对象", description = "分属性表实体表")
public class LibParameters extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String libId;
    private String name;
    private String value;
    private String unit;
    private String wbsCode;
    private String tempId;

    public LibParameters(String libId, String name, String value, String unit, String wbsCode, String tempId) {
        this.id = UUID.randomUUID().toString();
        this.libId = libId;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.wbsCode = wbsCode;
        this.tempId = tempId;
    }

    public LibParameters() {
    }
}
