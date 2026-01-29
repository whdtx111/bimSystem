package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_wbs_parameters_copy_color")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WBS颜色对象副本", description = "WBS颜色实体表")
public class WbsParametersCopyColor extends BaseEntity{

    private String id;
    private String wbsId;
    private String wbsCode;
    private String color;

    public WbsParametersCopyColor(String id, String wbsId, String wbsCode, String color) {
        this.id = UUID.randomUUID().toString();
        this.wbsId = wbsId;
        this.wbsCode = wbsCode;
        this.color = color;
    }

    public WbsParametersCopyColor() {

    }
}
