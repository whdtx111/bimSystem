package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.sp.entity.SubscriptionOrderStreams;
import org.springblade.modules.sp.mapper.SubscriptionOrderStreamsMapper;
import org.springblade.modules.sp.service.SubscriptionOrderStreamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class SubscriptionOrderStreamsServiceImpl extends ServiceImpl<SubscriptionOrderStreamsMapper, SubscriptionOrderStreams> implements SubscriptionOrderStreamsService {

    @Autowired
    private SubscriptionOrderStreamsMapper subscriptionOrderStreamsMapper;

    @Override
    public SubscriptionOrderStreams getById(String id) {
        try {
            return subscriptionOrderStreamsMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SubscriptionOrderStreams> getByOrderId(String orderId) {
        try {
            return subscriptionOrderStreamsMapper.getByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SubscriptionOrderStreams> getByStreamId(String streamId) {
        try {
            return subscriptionOrderStreamsMapper.getByStreamId(streamId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SubscriptionOrderStreams getByStreamIdAndOrderId(String orderId, String streamId) {
        try {
            return subscriptionOrderStreamsMapper.getByStreamIdAndOrderId(orderId, streamId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams) {
        try {
            return subscriptionOrderStreamsMapper.addSubscriptionOrderStreams(subscriptionOrderStreams);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubscriptionOrderStreams(SubscriptionOrderStreams subscriptionOrderStreams) {
        try {
            return subscriptionOrderStreamsMapper.updateSubscriptionOrderStreams(subscriptionOrderStreams);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSubscriptionOrderStreams(String id) {
        try {
            return subscriptionOrderStreamsMapper.deleteSubscriptionOrderStreams(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}