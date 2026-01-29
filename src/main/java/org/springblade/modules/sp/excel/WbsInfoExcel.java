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
package org.springblade.modules.sp.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Wbs任务表excel实体类
 *
 * @author wangc
 * @since 2023-04-23
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class WbsInfoExcel implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 编码
	 */
	@ApiModelProperty(value = "编码")
	@ExcelProperty("WBS编码")
	private String wbsCode;
	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	@ExcelProperty("任务名称")
	private String text;
	/**
	 * 英文任务名称
	 */
	@ApiModelProperty(value = "英文任务名称")
	@ExcelProperty("英文任务名称")
	private String englishText;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	@ExcelProperty("层级")
	private String wbsLevel;

	@ApiModelProperty(value = "计划开始时间")
	@ExcelProperty("计划开始时间")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date startDate;

	@ApiModelProperty(value = "计划结束时间")
	@ExcelProperty("计划结束时间")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date deadline;

	@ExcelProperty("构件-模型编码")
	private String modelCode; // 构件-模型编码
	@ExcelProperty("构件-模型名称")
	private String modelName; // 构件-模型名称
	@ExcelProperty("构件-模型编号")
	private String modelId; // 构件-模型编号
	@ExcelProperty("槽段类型")
	private String cdType; // 槽段类型
	@ExcelProperty("槽段设计深度")
	private String cdProfundity; // 槽段深度

}
