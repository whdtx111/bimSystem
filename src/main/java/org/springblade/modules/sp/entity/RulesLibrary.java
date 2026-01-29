package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@TableName("sp_rules_library")
@ApiModel(value = "RulesLibrary", description = "RulesLibrary")
public class RulesLibrary extends BaseEntity {

    private String id;

    private String name;

    private String[] streamIds;

    private String auth;

    private Integer status;

    private String user;

    private String source;

    private String description;

    private String createdUser;

    private Date updateTime;

    public RulesLibrary() {
        this.id = UUID.randomUUID().toString();
        this.updateTime = new Date();
    }

    public RulesLibrary(String id, String name, String[] streamIds, String auth, Integer status,String user, String source,String description,String createdUser) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.streamIds = streamIds;
        this.auth = auth;
        this.status = status;
        this.user = user;
        this.source = source;
        this.description = description;
        this.createdUser = createdUser;
        this.updateTime = new Date();
    }

}
