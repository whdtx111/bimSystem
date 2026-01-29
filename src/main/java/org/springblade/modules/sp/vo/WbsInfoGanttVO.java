package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "WbsInfoGanttVO对象", description = "WbsInfoGanttVO对象")
public class WbsInfoGanttVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long parent;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String wbsCode;

    @ApiModelProperty(value = "任务名称")
    private String text;

    @ApiModelProperty(value = "英文任务名称")
    private String englishText;

    @ApiModelProperty(value = "持续时间")
    private String duration;

    @ApiModelProperty(value = "完成度0.1 1百分比")
    private String progress;

    @ApiModelProperty(value = "开始时间")
    @JsonProperty("start_date")
    @JSONField(name="start_date")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date startDate;

    @ApiModelProperty(value = "最后期限")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private Date deadline;

    @ApiModelProperty(value = "gantt展开参数")
    private int open;
    private int infoType;
    private String wbsLevel;
    private String elementId;
    private String objectsId;
    private String streamsId;

}
