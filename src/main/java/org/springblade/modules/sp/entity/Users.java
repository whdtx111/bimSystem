/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.sp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import org.mindrot.jbcrypt.BCrypt;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.modules.sp.constant.SpConstant;

/**
 * 实体类
 *
 * @author wangc
 * @since 2023-09-13
 */
@Data
@ApiModel(value = "Users对象", description = "Users对象")
public class Users {

  private static final long serialVersionUID = 1L;

  private String id;
  private String suuid;
  @TableField("\"createdAt\"")
  private LocalDateTime createdAt;
  private String name;
  private String bio;
  private String company;
  private String email;
  private Boolean verified;
  private String avatar;
  private Object profiles;
  @TableField("\"passwordDigest\"")
  private String passwordDigest;
  private String ip;
  private String code;
  private String phone;
  @TableField("\"roleIds\"")
  private String[] roleIds;
  private String status;
  private Integer isOrder;

  public ServerAcl transform() {
    ServerAcl serverAcl = new ServerAcl();
    serverAcl.setUserId(id);
    serverAcl.setRole(SpConstant.USER_ROLE);
    return serverAcl;
  }

  public Users() {

  }
  public Users(String id,String suuid,String name,String bio,String company,String email,Boolean verified,String avatar,Object profiles,String passwordDigest,String ip,String code,String phone,String[] roleIds,String status,Integer isOrder) {
    this.id = id;
    this.suuid = suuid;
    this.name = name;
    this.bio = bio;
    this.company = company;
    this.email = email;
    this.verified = verified;
    this.avatar = avatar;
    this.profiles = profiles;
    this.passwordDigest = passwordDigest;
    this.ip = ip;
    this.code = code;
    this.phone = phone;
    this.roleIds = roleIds;
    this.status = status;
    this.isOrder = isOrder;
  }
}
