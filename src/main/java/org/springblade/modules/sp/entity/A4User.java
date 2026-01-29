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

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.modules.sp.constant.SpConstant;

/**
 * 4a_用户实体类
 *
 * @author wangc
 * @since 2023-09-13
 */
@Data
@TableName("sp_a4_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "A4User对象", description = "4a_用户")
public class A4User extends BaseEntity {

    private static final long serialVersionUID = 1L;

  private Long id;
  private String snebUserCode;
  private String snebName;
  private String snebEmail;
  private String snebPhone;

  public Users transform() {
    Users user = new Users();
    user.setId(IdUtil.nanoId(10));
    user.setSuuid(UUID.randomUUID().toString());
    user.setName(snebName);
    user.setCode(snebUserCode);
    user.setCompany(SpConstant.COMPANY_FLAG);
    user.setVerified(Boolean.FALSE);
    user.setEmail(snebPhone.concat(SpConstant.SP_EMAIL_SUFFIX));
    user.setPasswordDigest(BCrypt.hashpw(SpConstant.INIT_PASSWORD,BCrypt.gensalt()));
    user.setPhone(snebPhone);
    return user;
  }
}
