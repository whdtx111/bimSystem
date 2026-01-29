package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.SubscriptionOrder;
import org.springblade.modules.sp.vo.SubscriptionOrderDetailVO;

import java.util.Date;
import java.util.List;

public interface SubscriptionOrderService extends BaseService<SubscriptionOrder> {

    Page<SubscriptionOrderDetailVO> selectSubscriptionOrderList(String userId, Integer status, Date startTime, Date endTime, Integer pageSize, Integer currentPage);

    SubscriptionOrder getById(String id);

    SubscriptionOrder getByUserId(String userId);

    boolean insertSubscriptionOrder(SubscriptionOrder subscriptionConfig);

    boolean updateSubscriptionOrder(SubscriptionOrder subscriptionConfig);

    boolean deleteSubscriptionOrder(String id);

    boolean updateSubscriptionOrderStatus(String id, Integer status);

    boolean expandTotalStorage(String id,Long totalStorage);
}
