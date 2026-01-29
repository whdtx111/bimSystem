package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.UUID;

@Data
@TableName("sp_rules_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RulesDetail", description = "RulesDetail")
public class RulesDetail extends BaseEntity {

    private String id;

    private String pid;

    private String name;

    private String description;

    private Integer status;

    private String icon;



    public RulesDetail() {
        this.id = UUID.randomUUID().toString();
        this.status = 0;
    }

    public RulesDetail(String id, String pid, String name,String description,Integer status,String icon) {
        this.id = UUID.randomUUID().toString();
        this.pid = pid;
        this.name = name;
        this.description = description;
        this.status = 0;
        this.icon = icon;
    }

}
