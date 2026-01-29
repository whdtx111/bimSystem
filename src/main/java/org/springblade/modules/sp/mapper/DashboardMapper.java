package org.springblade.modules.sp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 看板数据Mapper接口
 * 
 * @author system
 * @since 2024-12-09
 */
@Mapper
public interface DashboardMapper {

    /**
     * 统计待处理批注数量（archived = false）
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 待处理批注数量
     */
    Integer countPendingComments(@Param("streamId") String streamId, 
                                  @Param("startDate") Date startDate, 
                                  @Param("endDate") Date endDate);

    /**
     * 统计未解决碰撞干扰数量
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 未解决碰撞干扰数量
     */
    Integer countUnresolvedCrashDetection(@Param("streamId") String streamId, 
                                          @Param("startDate") Date startDate, 
                                          @Param("endDate") Date endDate);

    /**
     * 统计近期模型迭代次数（commits数量）
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 模型迭代次数
     */
    Integer countModelIterations(@Param("streamId") String streamId, 
                                  @Param("startDate") Date startDate, 
                                  @Param("endDate") Date endDate);

    /**
     * 按日期分组统计碰撞检测趋势
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 按日期分组的碰撞检测数据
     */
    List<Map<String, Object>> getCrashDetectionTrendByDate(@Param("streamId") String streamId, 
                                                           @Param("startDate") Date startDate, 
                                                           @Param("endDate") Date endDate);

    /**
     * 按日期分组统计LOD合规得分
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 按日期分组的LOD合规得分数据
     */
    List<Map<String, Object>> getLODScoreByDate(@Param("streamId") String streamId, 
                                                 @Param("startDate") Date startDate, 
                                                 @Param("endDate") Date endDate);

    /**
     * 统计待处理问题分类
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 待处理问题分类数据
     */
    List<Map<String, Object>> getPendingIssueCategories(@Param("streamId") String streamId, 
                                                         @Param("startDate") Date startDate, 
                                                         @Param("endDate") Date endDate);

    /**
     * 按周分组统计团队数据同步活跃度
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 按周分组的团队活跃度数据
     */
    List<Map<String, Object>> getTeamSyncActivityByWeek(@Param("streamId") String streamId, 
                                                         @Param("startDate") Date startDate, 
                                                         @Param("endDate") Date endDate);

    /**
     * 获取高频变更模型列表
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 高频变更模型列表
     */
    List<Map<String, Object>> getHighFrequencyChangeModels(@Param("streamId") String streamId, 
                                                            @Param("startDate") Date startDate, 
                                                            @Param("endDate") Date endDate);

    /**
     * 按周分组统计设计裕落指数（基于批注解决率）
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 按周分组的设计裕落指数
     */
    List<Map<String, Object>> getDesignFalloffIndexByWeek(@Param("streamId") String streamId, 
                                                           @Param("startDate") Date startDate, 
                                                           @Param("endDate") Date endDate);

    /**
     * 按周分组统计新增批注趋势
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 按周分组的新增批注数据
     */
    List<Map<String, Object>> getNewCommentsTrendByWeek(@Param("streamId") String streamId, 
                                                         @Param("startDate") Date startDate, 
                                                         @Param("endDate") Date endDate);

    /**
     * 获取LOD得分统计（value_pass总和 / (value_pass + value_not_pass)总和）
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return LOD得分数据（包含total_pass, total_not_pass, score）
     */
    Map<String, Object> getLODScoreStatistics(@Param("streamId") String streamId,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);

    /**
     * 统计不符合审查规范的数量（sp_revit_lod中value_not_pass不为0）
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 不符合审查规范的数量
     */
    Integer countReviewNotPass(@Param("streamId") String streamId,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate);

    /**
     * 按周期统计commits数量
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return commits数量
     */
    Integer countCommitsByPeriod(@Param("streamId") String streamId,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate);

    /**
     * 按周期统计sp_template_version数量
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return template_version数量
     */
    Integer countTemplateVersionByPeriod(@Param("streamId") String streamId,
                                          @Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate);

    /**
     * 按周期统计新增批注数量
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 新增批注数量
     */
    Integer countNewCommentsByPeriod(@Param("streamId") String streamId,
                                      @Param("startDate") Date startDate,
                                      @Param("endDate") Date endDate);

    /**
     * 按周期统计已解决的碰撞数
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 已解决的碰撞数
     */
    Integer countResolvedCrashByPeriod(@Param("streamId") String streamId,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);

    /**
     * 按周期获取LOI/LOD得分
     * @param streamId 流ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return LOI/LOD得分数据（包含loi_score, lod_score）
     */
    Map<String, Object> getLODScoreByPeriod(@Param("streamId") String streamId,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);
}
