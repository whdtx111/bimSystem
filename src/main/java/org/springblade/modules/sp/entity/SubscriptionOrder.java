package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_subscription_order")
@ApiModel(value = "订阅记录实体")
public class SubscriptionOrder extends BaseEntity {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("配置ID")
    private String configId;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("已使用项目数")
    private Integer usedProjectNum;

    @ApiModelProperty("已使用用户数")
    private Integer usedUserNum;

    @ApiModelProperty("已使用存储空间")
    private Long usedStorage;

    @ApiModelProperty("总存储空间")
    private Long totalStorage;

    @ApiModelProperty("已使用时长")
    private Integer usedDuration;

    @ApiModelProperty("订阅时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    @ApiModelProperty("到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadTime;

    @ApiModelProperty("状态(0-生效中 1-已过期 2-停用)")
    private Integer status;

    public SubscriptionOrder() {
        this.id = UUID.randomUUID().toString();
    }

    public SubscriptionOrder(String configId, String userId, Integer usedProjectNum, Integer usedUserNum, Long usedStorage,Long totalStorage,Integer usedDuration,Date orderTime,Date deadTime,Integer status) {
        this.id = UUID.randomUUID().toString();
        this.configId = configId;
        this.userId = userId;
        this.usedProjectNum = usedProjectNum;
        this.usedUserNum = usedUserNum;
        this.usedStorage = usedStorage;
        this.usedDuration = usedDuration;
        this.totalStorage = totalStorage;
        this.orderTime = orderTime;
        this.deadTime = deadTime;
        this.status = status;
    }
}
