package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "UserAuth对象", description = "角色权限对象")
public class UserAuthVO{

    private static final long serialVersionUID = 1L;

    private String userId;

    private String roleId;

}
