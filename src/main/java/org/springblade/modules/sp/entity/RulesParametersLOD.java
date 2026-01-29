package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_rules_parameters_lod")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RulesParametersLOD", description = "RulesParametersLOD")
public class RulesParametersLOD extends BaseEntity {

    private String id;
    private String pid;
    private String streamId;
    private String branchId;
    private String fileId;
    private Integer status;
    private Date updateTime;

    public RulesParametersLOD() {
        this.id = UUID.randomUUID().toString();
        this.updateTime = new Date();
    }

    public RulesParametersLOD(String pid,String streamId, String branchId, String fileId, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.streamId = streamId;
        this.branchId = branchId;
        this.fileId = fileId;
        this.status = status;
        this.updateTime = new Date();
    }



}
