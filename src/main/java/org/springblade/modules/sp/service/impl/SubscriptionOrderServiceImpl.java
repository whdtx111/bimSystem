package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.SubscriptionOrder;
import org.springblade.modules.sp.mapper.SubscriptionOrderMapper;
import org.springblade.modules.sp.service.SubscriptionOrderService;
import org.springblade.modules.sp.vo.SubscriptionOrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
@DS("postgresql")
public class SubscriptionOrderServiceImpl extends BaseServiceImpl<SubscriptionOrderMapper, SubscriptionOrder> implements SubscriptionOrderService {

    @Autowired
    private SubscriptionOrderMapper subscriptionOrderMapper;

    @Override
    public Page<SubscriptionOrderDetailVO> selectSubscriptionOrderList(String userId, Integer status, Date startTime, Date endTime, Integer pageSize, Integer currentPage) {
        try {
            if (pageSize == null && currentPage == null) {
                pageSize = 1000;
                currentPage = 1;
            }
            PageHelper.startPage(currentPage, pageSize);
            return subscriptionOrderMapper.selectOrderList(userId,status,startTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SubscriptionOrder getById(String id) {
        try {
            return subscriptionOrderMapper.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SubscriptionOrder getByUserId(String userId) {
        try {
            return subscriptionOrderMapper.getByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertSubscriptionOrder(SubscriptionOrder subscriptionConfig) {
        try {
            subscriptionOrderMapper.insertSubscriptionOrder(subscriptionConfig);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubscriptionOrder(SubscriptionOrder subscriptionConfig) {
        try {
            subscriptionOrderMapper.updateSubscriptionOrder(subscriptionConfig);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSubscriptionOrder(String id) {
        try {
            subscriptionOrderMapper.deleteSubscriptionOrder(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubscriptionOrderStatus(String id, Integer status) {
        try {
            subscriptionOrderMapper.updateSubscriptionOrderStatus(id,status);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean expandTotalStorage(String id,Long totalStorage) {
        try {
            subscriptionOrderMapper.expandTotalStorage(id,totalStorage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
