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

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Wbs任务表实体类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Data
@TableName("sp_wbs_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WbsInfo对象", description = "Wbs任务表")
public class WbsInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 编码
	 */
	@ApiModelProperty(value = "编码")
	private String wbsCode;
	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	private String text;
	/**
	 * 英文任务名称
	 */
	@ApiModelProperty(value = "英文任务名称")
	private String englishText;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private String wbsLevel;
	/**
	 * 父code
	 */
	@ApiModelProperty(value = "父code")
	private String parentWbsCode;

	/**
	 * streamsId
	 */
	@ApiModelProperty(value = "streamsId")
	private String streamsId;

	/**
	 * elementId
	 */
	@ApiModelProperty(value = "elementId")
	private String elementId;

	/**
	 * objectsId
	 */
	@ApiModelProperty(value = "objectsId")
	private String objectsId;

	/**
	 * 1wbs，2构件
	 */
	@ApiModelProperty(value = "1wbs，2构件")
	private int infoType;

	private String modelCode; // 构件-模型编码
	private String modelName; // 构件-模型名称
	private String modelId; // 构件-模型编号
	private String cdType; // 槽段类型
	private String cdProfundity; // 槽段深度

	/***
	 * 甘特图 字段
	 * start>>>>>>>>>>>>>
	 */
	/**
	 * 持续时间
	 */
	@ApiModelProperty(value = "持续时间")
	private String duration;

	@ApiModelProperty(value = "完成度0.1 1百分比")
	private String progress;

	@ApiModelProperty(value = "排序")
	private String sortorder;

	@ApiModelProperty(value = "开始时间")
	@TableField("start_date")
	@JsonProperty("start_date")
	@JSONField(name="start_date")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	private Date startDate;

	@ApiModelProperty(value = "最后期限")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	private Date deadline;

	@ApiModelProperty(value = "当成实际开始用")
	@TableField("planned_start")
	@JsonProperty("planned_start")
	@JSONField(name="planned_start")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	private Date plannedStart;

	@ApiModelProperty(value = "当成实际结束用")
	@TableField("planned_end")
	@JsonProperty("planned_end")
	@JSONField(name="planned_end")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	private Date plannedEnd;

	@ApiModelProperty(value = "gantt展开参数")
	private int open;
	/***
	 * 甘特图 字段
	 * end>>>>>>>>>>>>>
	 */
}
