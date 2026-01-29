package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.SubscriptionConfig;
import org.springblade.modules.sp.mapper.SubscriptionConfigMapper;
import org.springblade.modules.sp.service.SubscriptionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@DS("postgresql")
public class SubscriptionConfigServiceImpl extends BaseServiceImpl<SubscriptionConfigMapper, SubscriptionConfig> implements SubscriptionConfigService {

    @Autowired
    private SubscriptionConfigMapper subscriptionConfigMapper;

    @Override
    public Page<SubscriptionConfig> selectSubscriptionConfigList(String name, Integer status,  Date createTime,  Date onlineTime, Date offlineTime,Integer pageSize, Integer currentPage) {
        try {
            if (pageSize == null && currentPage == null) {
                pageSize = 1000;
                currentPage = 1;
            }
            PageHelper.startPage(currentPage, pageSize);
            return subscriptionConfigMapper.selectSubscriptionConfigList(name, status,  createTime,  onlineTime, offlineTime);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SubscriptionConfig getById(String id) {
        try {
            return subscriptionConfigMapper.getById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertSubscriptionConfig(SubscriptionConfig subscriptionConfig) {
        try {
            return subscriptionConfigMapper.insertSubscriptionConfig(subscriptionConfig);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubscriptionConfig(SubscriptionConfig subscriptionConfig) {
        try {
            return subscriptionConfigMapper.updateSubscriptionConfig(subscriptionConfig);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSubscriptionConfig(String id) {
        try {
            return subscriptionConfigMapper.deleteSubscriptionConfig(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean batchDeleteSubscriptionConfig(List<String> ids) {
        try {
            return subscriptionConfigMapper.batchDeleteSubscriptionConfig(ids);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSubscriptionConfigStatus(String id, Integer status) {
        try {
            return subscriptionConfigMapper.updateSubscriptionConfigStatus(id, status);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
