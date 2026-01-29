package org.springblade.modules.sp.vo;

import lombok.Data;
import org.springblade.modules.sp.entity.SubscriptionConfig;

import java.util.Date;

@Data
public class UserOrderDetailVO {

    private SubscriptionConfig subscriptionConfig;

    private Date deadTime;

    private Long usedStorage;

    private Long totalStorage;

    private Integer usedProjectNum;

    private Integer totalProjectNum;


}
