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
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author wangc
 * @since 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Objects对象", description = "Objects对象")
public class Objects extends BaseEntity {

    private static final long serialVersionUID = 1L;

  private String id;
  private String streamId;
  private String units;
  private String category;
  private String family;
  private String type;
  private String elementId;
  private String parameters;
  private String resourceId;
  private String referencedId;


}
