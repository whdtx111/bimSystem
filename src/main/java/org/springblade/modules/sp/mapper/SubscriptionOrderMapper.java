package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.SubscriptionOrder;
import org.springblade.modules.sp.vo.SubscriptionOrderDetailVO;

import java.util.Date;
import java.util.List;
@Mapper
public interface SubscriptionOrderMapper extends BaseMapper<SubscriptionOrder> {

    Page<SubscriptionOrderDetailVO> selectOrderList(@Param("userId") String userId, @Param("status") Integer status, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    SubscriptionOrder getById(String id);

    SubscriptionOrder getByUserId(String userId);

    boolean insertSubscriptionOrder(SubscriptionOrder subscriptionConfig);

    boolean updateSubscriptionOrder(SubscriptionOrder subscriptionConfig);

    boolean deleteSubscriptionOrder(String id);

    boolean updateSubscriptionOrderStatus(String id,Integer status);

    boolean expandTotalStorage(String id,Long totalStorage);
}
