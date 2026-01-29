package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.annotation.JSONField;
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
@ApiModel(value = "WbsInfoUEVO对象", description = "WbsInfoUEVO对象")
public class WbsInfoUEVO implements Serializable {
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

    @ApiModelProperty(value = "持续时间")
    private String duration;

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
    private int infoType;
    private String elementId;
    private String objectsId;
    private String streamsId;

}
