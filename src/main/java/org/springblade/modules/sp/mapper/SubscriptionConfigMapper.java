package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.SubscriptionConfig;

import java.util.Date;
import java.util.List;
@Mapper
public interface SubscriptionConfigMapper extends BaseMapper<SubscriptionConfig> {

    Page<SubscriptionConfig> selectSubscriptionConfigList(@Param("name") String name, @Param("status") Integer status, @Param("createTime") Date createTime,@Param("onlineTime") Date onlineTime,@Param("offlineTime") Date offlineTime);

    SubscriptionConfig getById(String id);

    boolean insertSubscriptionConfig(SubscriptionConfig subscriptionConfig);

    boolean updateSubscriptionConfig(SubscriptionConfig subscriptionConfig);

    boolean deleteSubscriptionConfig(String id);

    boolean batchDeleteSubscriptionConfig(List<String> ids);

    boolean updateSubscriptionConfigStatus(String id, Integer status);
}
