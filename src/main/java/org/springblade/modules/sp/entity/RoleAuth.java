package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@TableName("sl_role_auth")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "角色关系对象", description = "项目人员关系实体表")
public class RoleAuth extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String authId;

    private String roleId;

    private String authName;

    private String authLevel;

    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;
    private Integer status;

    public RoleAuth(String authId, String roleId, String authName,String authLevel,String modifyUser,Date modifyTime,Integer status) {
        this.authId = UUID.randomUUID().toString();
        this.roleId = roleId;
        this.authName = authName;
        this.authLevel = authLevel;
        this.modifyUser = modifyUser;
        this.modifyTime = new Date();
        this.status = status;
    }

    public RoleAuth() {
        this.authId = UUID.randomUUID().toString();
    }

}
