package org.springblade.modules.sp.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.CrashDetection;
import org.springblade.modules.sp.mapper.CrashDetectionMapper;
import org.springblade.modules.sp.service.CrashDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 碰撞检测Service实现类
 * 
 * @author Yi
 * @since 2024-11-12
 */
@Slf4j
@Service
public class CrashDetectionServiceImpl implements CrashDetectionService {

    @Autowired
    private CrashDetectionMapper crashDetectionMapper;

    @Override
    public CrashDetection getById(String id) {
        try {
            return crashDetectionMapper.getById(id);
        } catch (Exception e) {
            log.error("根据id查询碰撞检测数据失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public CrashDetection getByStreamBranchCommit(String streamId, String branchId, String commitId) {
        try {
            return crashDetectionMapper.getByStreamBranchCommit(streamId, branchId, commitId);
        } catch (Exception e) {
            log.error("根据streamId/branchId/commitId查询碰撞检测数据失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<CrashDetection> getByStreamId(String streamId) {
        try {
            return crashDetectionMapper.getByStreamId(streamId);
        } catch (Exception e) {
            log.error("根据streamId查询碰撞检测数据列表失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<CrashDetection> getAllCrashDetection() {
        try {
            return crashDetectionMapper.getAllCrashDetection();
        } catch (Exception e) {
            log.error("查询所有碰撞检测数据失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCrashDetection(CrashDetection crashDetection) {
        try {
            int result = crashDetectionMapper.addCrashDetection(crashDetection);
            log.info("新增碰撞检测数据成功，ID: {}", crashDetection.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("新增碰撞检测数据失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        try {
            int result = crashDetectionMapper.deleteById(id);
            log.info("根据id删除碰撞检测数据成功，ID: {}", id);
            return result > 0;
        } catch (Exception e) {
            log.error("根据id删除碰撞检测数据失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByStreamBranchCommit(String streamId, String branchId, String commitId) {
        try {
            int result = crashDetectionMapper.deleteByStreamBranchCommit(streamId, branchId, commitId);
            log.info("根据streamId/branchId/commitId删除碰撞检测数据成功");
            return result > 0;
        } catch (Exception e) {
            log.error("根据streamId/branchId/commitId删除碰撞检测数据失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCrashDetection(CrashDetection crashDetection) {
        try {
            crashDetection.setModifyTime(new Date());
            int result = crashDetectionMapper.updateCrashDetection(crashDetection);
            log.info("更新碰撞检测数据成功，ID: {}", crashDetection.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新碰撞检测数据失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusAndData(String id, Integer status, JSONObject data) {
        try {
            CrashDetection crashDetection = crashDetectionMapper.getById(id);
            if (crashDetection == null) {
                log.warn("更新状态和数据失败，找不到ID: {}", id);
                return false;
            }
            
            crashDetection.setStatus(status);
            crashDetection.setData(data);
            crashDetection.setModifyTime(new Date());
            
            int result = crashDetectionMapper.updateCrashDetection(crashDetection);
            log.info("更新碰撞检测状态和数据成功，ID: {}, status: {}", id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新碰撞检测状态和数据失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
