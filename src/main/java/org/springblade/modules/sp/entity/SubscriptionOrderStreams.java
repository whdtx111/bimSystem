package org.springblade.modules.sp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("sp_subscription_order_streams")
@ApiModel(value = "订阅记录实体")
public class SubscriptionOrderStreams extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String streamId;
    private String name;
    private Long usedStorage;
    private Long totalStorage;
    //订阅套餐配置最大人员数量
    private Integer projectNum;
//    当前项目人员数量
    private Integer userNum;
    private String owner;
    private Date createTime;
    private Date deadTime;

    public SubscriptionOrderStreams() {
        this.id = UUID.randomUUID().toString();
        this.createTime = new Date();
    }

    public SubscriptionOrderStreams(String orderId, String streamId, String name, Long usedStorage,Long totalStorage,Integer projectNum, Integer userNum, String owner, Date deadTime) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.streamId = streamId;
        this.name = name;
        this.usedStorage = usedStorage;
        this.totalStorage = totalStorage;
        this.projectNum = projectNum;
        this.userNum = userNum;
        this.owner = owner;
        this.deadTime = deadTime;
        this.createTime = new Date();
    }
}
