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
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 4a_用户实体类
 *
 * @author wangc
 * @since 2023-11-22
 */
@Data
@TableName("sp_snapshot")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Snapshot对象", description = "Snapshot对象")
public class Snapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

  private Long id;
  private String userId;
    /**
     * 快照名称
     */
    @ApiModelProperty(value = "快照名称")
    private String snapshotName;
    /**
     * 1-着色,2-属性
     */
    @ApiModelProperty(value = "1-着色,2-属性")
    private String snapshotType;
    /**
     * 色值
     */
    @ApiModelProperty(value = "色值")
    private String colorValue;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
  private String objId;
  private String streamId;
  private String commitId;
    /**
     * 快照id
     */
    @ApiModelProperty(value = "快照id")
    private String snapshotUid;


}
