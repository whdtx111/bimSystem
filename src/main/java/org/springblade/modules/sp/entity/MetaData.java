package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_meta")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "模版元数据对象", description = "模板元数据实体表")
public class MetaData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String guide;
    private String name;
    private String val;
    private String valRange;
    private String dataType;
    private String units;

    private String modifyUser;
    private Date modifyTime;
    private Integer status;


    public MetaData(String id, String guide, String name, String val, String valRange, String dataType, String units, String modifyUser) {
        this.id = UUID.randomUUID().toString();
        this.guide = guide;
        this.name = name;
        this.val = val;
        this.valRange = valRange;
        this.dataType = dataType;
        this.units = units;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = 0;
    }
}
