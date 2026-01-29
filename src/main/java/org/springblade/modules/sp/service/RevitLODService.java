package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.entity.RevitLOD;

import java.util.List;
import java.util.Map;

/**
 * RevitLOD检测数据服务接口
 */
public interface RevitLODService extends BaseService<RevitLOD> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RevitLOD getById(String id);

    /**
     * 根据checkId查询
     * @param checkId
     * @return
     */
    RevitLOD getByCheckId(String checkId);

    /**
     * 根据streamId、branchId、commitId查询列表
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    RevitLOD getByStreamBranchCommit(String streamId, String branchId, String commitId);

    /**
     * 根据streamId、branchId、commitId、checkId查询记录
     * @param streamId
     * @param branchId
     * @param commitId
     * @param checkId
     * @return
     */
    RevitLOD getByStreamBranchCommitAndCheckId(String streamId, String branchId, String commitId, String checkId);

    /**
     * 根据streamId、branchId、commitId查询所有记录列表
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    List<RevitLOD> getListByStreamBranchCommit(String streamId, String branchId, String commitId);

    /**
     * 根据streamId、branchId查询最新的记录列表（不需要commitId）
     * @param streamId
     * @param branchId
     * @return
     */
    List<RevitLOD> getLatestListByStreamBranch(String streamId, String branchId);

    /**
     * 根据streamId查询列表
     * @param streamId
     * @return
     */
    List<RevitLOD> getByStreamId(String streamId);

    /**
     * 新增
     * @param revitLOD
     * @return
     */
    boolean addRevitLOD(RevitLOD revitLOD);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 根据checkId删除
     * @param checkId
     * @return
     */
    boolean deleteByCheckId(String checkId);

    /**
     * 更新
     * @param revitLOD
     * @return
     */
    boolean updateRevitLOD(RevitLOD revitLOD);

    /**
     * 获取所有数据
     * @return
     */
    List<RevitLOD> getAllRevitLOD();

    /**
     * 批量保存RevitLOD检测数据
     * @param jsonDataList JSON数组数据
     * @return
     */
    List<RevitLOD> batchSaveRevitLOD(List<Map<String, Object>> jsonDataList);

    /**
     * 根据RevitLOD主键id获取格式化输出数据
     * @param id RevitLOD主键ID
     * @return 格式化后的输出数据
     */
    List<Map<String, Object>> getFormattedOutputById(String id);


    /**
     * 根据streamId、branchId获取格式化输出数据（获取最新记录，不需要commitId）
     * @param streamId
     * @param branchId
     * @return
     */
    List<Map<String, Object>> getFormattedOutput(String streamId, String branchId);

    /**
     * 根据streamId、branchId、commitId获取格式化输出数据
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    List<Map<String, Object>> getFormattedOutputWithCommitId(String streamId, String branchId, String commitId);

    /**
     * 根据elementId、streamId、branchId查找包含该elementId的templateId列表
     * @param elementId 元素ID
     * @param streamId 流ID
     * @param branchId 分支ID
     * @return templateId数组
     */
    List<NewTemplate> getTemplateIdsByElementId(String elementId, String streamId, String branchId);
}
