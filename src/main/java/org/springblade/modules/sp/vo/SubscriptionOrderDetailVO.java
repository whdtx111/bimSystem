package org.springblade.modules.sp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.modules.sp.entity.SubscriptionConfig;
import org.springblade.modules.sp.entity.SubscriptionOrder;

@Data
@ApiModel(value = "订阅订单详情VO")
public class SubscriptionOrderDetailVO extends SubscriptionOrder {

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "订阅配置名称")
    private String configName;

    @ApiModelProperty(value = "订阅配置价格")
    private String configPrice;

    @ApiModelProperty(value = "订阅配置项目数")
    private Integer configProjectNum;

    @ApiModelProperty(value = "订阅配置用户数")
    private Integer configUserNum;

    @ApiModelProperty(value = "订阅配置存储空间")
    private Integer configStorage;

    @ApiModelProperty(value = "订阅配置时长")
    private Integer configDuration;

    @ApiModelProperty(value = "订阅配置存储空间单位")
    private String configStorageUnit;
}