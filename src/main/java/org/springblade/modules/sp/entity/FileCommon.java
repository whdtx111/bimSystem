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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

/**
 * bim-obj-文件实体类
 *
 * @author wangc
 * @since 2023-07-11
 */
@Data
@TableName("sp_file_common")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FileCommon对象", description = "bim-obj-文件")
public class FileCommon extends BaseEntity {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String streamsId;
  private String objectsId;
  private String fileName;
  private String fileType;
  private Long fileSize;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String fileLink;
  private String fileDomain;
  private String fileOriginalName;
  private String commitsId;
  private String resourceType;
  private String elementId;
  private String remark;
  private String userId;
  private String fileCoord;



}
