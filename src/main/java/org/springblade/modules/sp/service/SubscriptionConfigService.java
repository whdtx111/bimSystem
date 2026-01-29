package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.SubscriptionConfig;

import java.util.Date;
import java.util.List;

public interface SubscriptionConfigService extends BaseService<SubscriptionConfig> {

    Page<SubscriptionConfig> selectSubscriptionConfigList(String name, Integer status,  Date createTime,  Date onlineTime, Date offlineTime,Integer pageSize, Integer currentPage);

    SubscriptionConfig getById(String id);

    boolean insertSubscriptionConfig(SubscriptionConfig subscriptionConfig);

    boolean updateSubscriptionConfig(SubscriptionConfig subscriptionConfig);

    boolean deleteSubscriptionConfig(String id);

    boolean batchDeleteSubscriptionConfig(List<String> ids);

    boolean updateSubscriptionConfigStatus(String id, Integer status);
}
