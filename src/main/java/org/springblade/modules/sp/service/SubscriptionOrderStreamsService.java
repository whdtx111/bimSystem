package org.springblade.modules.sp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.sp.entity.SubscriptionOrderStreams;

import java.util.List;

public interface SubscriptionOrderStreamsService extends IService<SubscriptionOrderStreams> {

    boolean addSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams);

    boolean updateSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams);

    boolean deleteSubscriptionOrderStreams(String id);

    SubscriptionOrderStreams getById(String id);

    SubscriptionOrderStreams getByStreamIdAndOrderId(String streamId,String orderId);

    List<SubscriptionOrderStreams> getByOrderId(String orderId);

    List<SubscriptionOrderStreams> getByStreamId(String streamId);
}