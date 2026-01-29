package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_element_wbs")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ElementWbs对象", description = "ElementWbs实体表")
public class ElementWbs extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String[] elementId;
    private String wbsCode;
    private String branchId;
    private String tempId;
    private Integer status;

    public ElementWbs(String id, String[] elementId, String wbsCode, String branchId, String tempId, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.elementId = elementId;
        this.wbsCode = wbsCode;
        this.branchId = branchId;
        this.tempId = tempId;
        this.status =  0;
    }
    public ElementWbs() {
    }
}
