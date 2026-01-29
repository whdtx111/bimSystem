package org.springblade.modules.sp.service;

import com.alibaba.fastjson.JSONObject;
import org.springblade.modules.sp.entity.CrashDetection;

import java.util.List;

/**
 * 碰撞检测Service接口
 * 
 * @author Yi
 * @since 2024-11-12
 */
public interface CrashDetectionService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CrashDetection getById(String id);

    /**
     * 根据streamId、branchId、commitId查询
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    CrashDetection getByStreamBranchCommit(String streamId, String branchId, String commitId);

    /**
     * 根据streamId查询列表
     * @param streamId
     * @return
     */
    List<CrashDetection> getByStreamId(String streamId);

    /**
     * 查询所有数据
     * @return
     */
    List<CrashDetection> getAllCrashDetection();

    /**
     * 新增
     * @param crashDetection
     * @return
     */
    boolean addCrashDetection(CrashDetection crashDetection);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 根据streamId、branchId、commitId删除
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    boolean deleteByStreamBranchCommit(String streamId, String branchId, String commitId);

    /**
     * 更新
     * @param crashDetection
     * @return
     */
    boolean updateCrashDetection(CrashDetection crashDetection);

    /**
     * 更新状态和数据
     * @param id
     * @param status
     * @param data
     * @return
     */
    boolean updateStatusAndData(String id, Integer status, JSONObject data);
}
