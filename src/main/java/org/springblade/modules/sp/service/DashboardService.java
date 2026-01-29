package org.springblade.modules.sp.service;

import java.util.List;
import java.util.Map;

/**
 * 看板数据服务接口
 * 
 * @author system
 * @since 2024-12-09
 */
public interface DashboardService {

    /**
     * 获取顶部统计卡片数据
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 统计卡片数据列表
     */
    List<Map<String, Object>> getStatistics(String streamId, String type);

    /**
     * 获取设计震荡指数
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 设计震荡指数数组数据 [{x, versionChangeNum, problemNum}, ...]
     */
    List<Map<String, Object>> getDesignFalloffIndex(String streamId, String type);

    /**
     * 获取碰撞检测趋势
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 碰撞检测趋势数组数据 [{x, resolved, remaining}, ...]
     */
    List<Map<String, Object>> getCrashDetectionTrend(String streamId, String type);

    /**
     * 获取LOI/LOD合规得分
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return LOI/LOD合规得分数组数据 [{x, loiScore, lodScore}, ...]
     */
    List<Map<String, Object>> getLODComplianceScore(String streamId, String type);

    /**
     * 获取待处理问题分类
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 待处理问题分类饼图数据，格式为 [{name, color, num(百分比)}, ...]
     */
    List<Map<String, Object>> getPendingIssueCategories(String streamId, String type);

    /**
     * 获取团队数据同步活跃度
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 团队数据同步活跃度数组数据 [{x, count}, ...]
     */
    List<Map<String, Object>> getTeamSyncActivity(String streamId, String type);

    /**
     * 获取高频变更模型
     * @param streamId 流ID
     * @param type 时间类型：month（30天）或 week（7天）
     * @return 高频变更模型列表数据
     */
    List<Map<String, Object>> getHighFrequencyChangeModels(String streamId, String type);
}
