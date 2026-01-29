package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_rules_parameters")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RulesParameters", description = "RulesParameters")
public class RulesParameters extends BaseEntity {

    private String id;
    private String detailId;
    private String name;
    private String relation;
    private String value1;
    private String value2;
    private Integer status;
    private Date updateTime;

    public RulesParameters() {
        this.id = UUID.randomUUID().toString();
        this.updateTime = new Date();
    }

    public RulesParameters(String id, String detailId, String name, String relation, String value1, String value2, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.detailId = detailId;
        this.name = name;
        this.relation = relation;
        this.value1 = value1;
        this.value2 = value2;
        this.status = status;
        this.updateTime = new Date();
    }

}
