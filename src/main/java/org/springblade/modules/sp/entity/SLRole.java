package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sl_role")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "角色关系对象", description = "项目人员关系实体表")
public class SLRole extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String roleId;
    private String roleName;
    private String description;
    private String streamId;
    @TableField("modify_user")
    private String modifyUser;
    @TableField("modify_time")
    private Date modifyTime;
    private Integer status;

    public SLRole() {
        this.roleId = UUID.randomUUID().toString();
    }

    public SLRole(String roleId, String roleName,String description,String streamId,Integer status) {
        this.roleId = UUID.randomUUID().toString();
        this.roleName = roleName;
        this.description = description;
        this.streamId = streamId;
        this.status = status;
    }

}
