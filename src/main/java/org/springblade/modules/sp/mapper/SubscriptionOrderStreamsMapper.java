package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.SubscriptionOrderStreams;


import java.util.Date;
import java.util.List;

@Mapper
public interface SubscriptionOrderStreamsMapper extends BaseMapper<SubscriptionOrderStreams> {

    boolean addSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams);

    boolean updateSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams);

    boolean deleteSubscriptionOrderStreams(String id);

    SubscriptionOrderStreams getById(String id);

    SubscriptionOrderStreams getByStreamIdAndOrderId(String streamId,String orderId);

    List<SubscriptionOrderStreams> getByOrderId(String orderId);

    List<SubscriptionOrderStreams> getByStreamId(String streamId);
}
