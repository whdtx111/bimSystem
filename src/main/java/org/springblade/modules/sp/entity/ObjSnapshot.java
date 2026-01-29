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

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模型 色值 和 剖切快照实体类
 *
 * @author wangc
 * @since 2023-12-06
 */
@Data
@TableName("sp_obj_snapshot")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ObjSnapshot对象", description = "模型 色值 和 剖切快照")
public class ObjSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

  @JsonSerialize(
          using = ToStringSerializer.class
  )
  private Long id;
  @JsonSerialize(
          using = ToStringSerializer.class
  )
  private String userId;
    /**
     * 快照名称
     */
    @ApiModelProperty(value = "快照名称")
    private String snapshotName;
    /**
     * 模型数据
     */
    @ApiModelProperty(value = "模型数据")
    private String modelData;
    /**
     * 颜色数据
     */
    @ApiModelProperty(value = "颜色数据")
    private String colorData;

    private String cameraData;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    private String url;

    private String streamId;
    private String commitId;



}
