package org.springblade.modules.sp.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@TableName("sp_bimobjvo")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BimObjVO对象", description = "BimObjVO对象")
public class BimObjVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String objId;
    private String referencedId;
    private String category;
    private String family;
    private String type;
    private String elementId;
    private List<String> definitionObjIdList;
    private List<String> definitionElementIdList;



}
