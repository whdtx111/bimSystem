package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息带角色名称VO
 *
 * @author system
 */
@Data
@ApiModel(value = "UserWithRoleNameVO对象", description = "用户信息带角色名称VO")
public class UserWithRoleNameVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户UUID")
    private String suuid;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "个人简介")
    private String bio;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否验证")
    private Boolean verified;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "配置信息")
    private Object profiles;

    @ApiModelProperty(value = "密码摘要")
    private String passwordDigest;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "用户编码")
    private String code;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "角色ID数组")
    private String[] roleIds;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否订阅")
    private Integer isOrder;

    @ApiModelProperty(value = "角色名称（逗号分隔）")
    private String roleName;
}
