package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.mapper.DashboardMapper;
import org.springblade.modules.sp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 看板数据服务实现类
 * 
 * @author system
 * @since 2024-12-09
 */
@Service
@DS("postgresql")
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    /**
     * 根据类型计算天数
     * @param type month或week
     * @return 天数
     */
    private int getDaysByType(String type) {
        return "month".equals(type) ? 30 : 7;
    }

    /**
     * 获取周期列表（最近6周或6个月）
     * @param type week或month
     * @return 周期列表
     */
    private List<String> getPeriodList(String type) {
        List<String> periods = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        if ("week".equals(type)) {
            // 获取最近6周（包含当前周）
            // 使用简单的周数计算，避免跨年问题
            for (int i = 5; i >= 0; i--) {
                Calendar weekCal = (Calendar) cal.clone();
                weekCal.add(Calendar.DAY_OF_YEAR, -i * 7);
                
                // 获取该周的年份和周数
                int year = weekCal.get(Calendar.YEAR);
                int weekNum = weekCal.get(Calendar.WEEK_OF_YEAR);
                
                // 修正：如果是1月但周数很大（52、53），说明是上一年的最后几周
                int month = weekCal.get(Calendar.MONTH);
                if (month == Calendar.JANUARY && weekNum >= 52) {
                    year--;
                }
                // 如果是12月但周数是1，说明是下一年的第一周
                if (month == Calendar.DECEMBER && weekNum == 1) {
                    year++;
                }
                
                periods.add("第" + weekNum + "周");
            }
        } else {
            // 获取最近6个月
            for (int i = 5; i >= 0; i--) {
                Calendar monthCal = (Calendar) cal.clone();
                monthCal.add(Calendar.MONTH, -i);
                int month = monthCal.get(Calendar.MONTH) + 1;
                periods.add(String.format("%02d月", month));
            }
        }
        
        return periods;
    }

    /**
     * 获取周期的开始时间（从当前时间往前推完整周期）
     * @param type week或month
     * @param periodsBack 往前推几个周期（0代表当前周期，1代表上一个周期，以此类推）
     * @return 开始时间
     */
    private Date getPeriodStartDate(String type, int periodsBack) {
        Calendar cal = Calendar.getInstance();
        
        if ("week".equals(type)) {
            // 计算往前推的天数（每周7天）
            int daysToSubtract = periodsBack * 7;
            cal.add(Calendar.DAY_OF_YEAR, -daysToSubtract);
            // 对于当前周期（periodsBack=0），开始时间为7天前
            if (periodsBack == 0) {
                cal.add(Calendar.DAY_OF_YEAR, -6);
            } else {
                // 对于历史周期，结束日期向前推7天为开始日期
                cal.add(Calendar.DAY_OF_YEAR, -6);
            }
        } else {
            // month类型：往前推完整月份
            cal.add(Calendar.MONTH, -periodsBack);
            // 设置为该月的1号
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取周期的结束时间（从当前时间往前推完整周期）
     * @param type week或month
     * @param periodsBack 往前推几个周期（0代表当前周期，1代表上一个周期，以此类推）
     * @return 结束时间
     */
    private Date getPeriodEndDate(String type, int periodsBack) {
        Calendar cal = Calendar.getInstance();
        
        if ("week".equals(type)) {
            // 计算往前推的天数（每周7天）
            int daysToSubtract = periodsBack * 7;
            cal.add(Calendar.DAY_OF_YEAR, -daysToSubtract);
            // 对于当前周期（periodsBack=0），结束时间就是今天
            // 对于历史周期，向前推对应天数后就是该周期的结束时间
        } else {
            // month类型：往前推完整月份
            cal.add(Calendar.MONTH, -periodsBack);
            // 设置为该月的最后一天
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取当前周期的开始时间
     * @param days 天数
     * @return 开始时间
     */
    private Date getCurrentPeriodStartDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前周期的结束时间
     * @return 结束时间
     */
    private Date getCurrentPeriodEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取上一周期的开始时间
     * @param days 天数
     * @return 开始时间
     */
    private Date getPreviousPeriodStartDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days * 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取上一周期的结束时间
     * @param days 天数
     * @return 结束时间
     */
    private Date getPreviousPeriodEndDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 计算环比变化率
     * @param current 当前值
     * @param previous 上期值
     * @return 变化率（百分比）
     */
    private int calculateCompareRate(int current, int previous) {
        if (previous == 0) {
            return current > 0 ? 100 : 0;
        }
        double rate = ((double) (current - previous) / previous) * 100;
        return (int) Math.round(rate);
    }

    @Override
    public List<Map<String, Object>> getStatistics(String streamId, String type) {
        int days = getDaysByType(type);
        Date currentStart = getCurrentPeriodStartDate(days);
        Date currentEnd = getCurrentPeriodEndDate();
        Date previousStart = getPreviousPeriodStartDate(days);
        Date previousEnd = getPreviousPeriodEndDate(days);

        List<Map<String, Object>> result = new ArrayList<>();

        // 1. 待处理批注
        Integer currentPendingComments = dashboardMapper.countPendingComments(streamId, currentStart, currentEnd);
        Integer previousPendingComments = dashboardMapper.countPendingComments(streamId, previousStart, previousEnd);
        currentPendingComments = currentPendingComments != null ? currentPendingComments : 0;
        previousPendingComments = previousPendingComments != null ? previousPendingComments : 0;
        
        Map<String, Object> annotationCard = new LinkedHashMap<>();
        annotationCard.put("title", "待处理批注");
        annotationCard.put("num", currentPendingComments);
        annotationCard.put("icon", "icon-pizhu");
        annotationCard.put("compareRate", calculateCompareRate(currentPendingComments, previousPendingComments));
        annotationCard.put("color", "#E6A23C");
        annotationCard.put("type", "annotation");
        result.add(annotationCard);

        // 2. 未解决碰撞干扰
        Integer currentCrashDetection = dashboardMapper.countUnresolvedCrashDetection(streamId, currentStart, currentEnd);
        Integer previousCrashDetection = dashboardMapper.countUnresolvedCrashDetection(streamId, previousStart, previousEnd);
        currentCrashDetection = currentCrashDetection != null ? currentCrashDetection : 0;
        previousCrashDetection = previousCrashDetection != null ? previousCrashDetection : 0;
        int crashDiff = currentCrashDetection - previousCrashDetection;
        
        Map<String, Object> interferenceCard = new LinkedHashMap<>();
        interferenceCard.put("title", "未解决碰撞干扰");
        interferenceCard.put("num", currentCrashDetection);
        interferenceCard.put("icon", "icon-pengzhuangganrao");
        interferenceCard.put("compareRate", calculateCompareRate(currentCrashDetection, previousCrashDetection));
        interferenceCard.put("compareNum", Math.abs(crashDiff));
        interferenceCard.put("color", "#7C3AED");
        interferenceCard.put("type", "interference");
        result.add(interferenceCard);

        // 3. LOD得分（value_pass总和 / (value_pass + value_not_pass)总和 * 100）
        Map<String, Object> currentLODData = dashboardMapper.getLODScoreStatistics(streamId, currentStart, currentEnd);
        Map<String, Object> previousLODData = dashboardMapper.getLODScoreStatistics(streamId, previousStart, previousEnd);
        
        int currentLODScore = 0;
        int previousLODScore = 0;
        
        if (currentLODData != null && currentLODData.get("score") != null) {
            currentLODScore = ((Number) currentLODData.get("score")).intValue();
        }
        if (previousLODData != null && previousLODData.get("score") != null) {
            previousLODScore = ((Number) previousLODData.get("score")).intValue();
        }
        
        Map<String, Object> scoreCard = new LinkedHashMap<>();
        scoreCard.put("title", "模型合规得分");
        scoreCard.put("num", currentLODScore);
        scoreCard.put("icon", "icon-hegui");
        scoreCard.put("compareRate", calculateCompareRate(currentLODScore, previousLODScore));
        scoreCard.put("color", "#67C23A");
        scoreCard.put("type", "score");
        result.add(scoreCard);

        // 4. 近期模型迭代次数
        Integer currentIterations = dashboardMapper.countModelIterations(streamId, currentStart, currentEnd);
        Integer previousIterations = dashboardMapper.countModelIterations(streamId, previousStart, previousEnd);
        currentIterations = currentIterations != null ? currentIterations : 0;
        previousIterations = previousIterations != null ? previousIterations : 0;
        
        Map<String, Object> iterationCard = new LinkedHashMap<>();
        iterationCard.put("title", "近期模型迭代次数");
        iterationCard.put("num", currentIterations);
        iterationCard.put("icon", "icon-biangeng");
        iterationCard.put("compareRate", calculateCompareRate(currentIterations, previousIterations));
        iterationCard.put("color", "#4080FF");
        iterationCard.put("type", "iterationCount");
        result.add(iterationCard);

        log.info("获取统计卡片数据成功, streamId: {}, type: {}", streamId, type);
        return result;
    }

    @Override
    public List<Map<String, Object>> getDesignFalloffIndex(String streamId, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> periodList = getPeriodList(type);
        
        // 遍历最近6周或6个月
        for (int i = 5; i >= 0; i--) {
            Date periodStart = getPeriodStartDate(type, i);
            Date periodEnd = getPeriodEndDate(type, i);
            
            // 获取该周期内的commits数量（设计变更数）
            Integer commitsCount = dashboardMapper.countCommitsByPeriod(streamId, periodStart, periodEnd);
            commitsCount = commitsCount != null ? commitsCount : 0;
            
            // 获取该周期内的新增批注数量（新增问题数）
            Integer commentsCount = dashboardMapper.countNewCommentsByPeriod(streamId, periodStart, periodEnd);
            commentsCount = commentsCount != null ? commentsCount : 0;
            
            Map<String, Object> dataItem = new LinkedHashMap<>();
            dataItem.put("x", periodList.get(5 - i));
            dataItem.put("versionChangeNum", commitsCount);
            dataItem.put("problemNum", commentsCount);
            result.add(dataItem);
        }

        log.info("获取设计震荡指数成功, streamId: {}, type: {}", streamId, type);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCrashDetectionTrend(String streamId, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> periodList = getPeriodList(type);
        
        // 遍历最近6周或6个月
        for (int i = 5; i >= 0; i--) {
            Date periodStart = getPeriodStartDate(type, i);
            Date periodEnd = getPeriodEndDate(type, i);
            
            // 获取该周期内已解决的碰撞数
            Integer resolvedCount = dashboardMapper.countResolvedCrashByPeriod(streamId, periodStart, periodEnd);
            resolvedCount = resolvedCount != null ? resolvedCount : 0;
            
            // 获取该周期内未解决的碰撞数
            Integer remainingCount = dashboardMapper.countUnresolvedCrashDetection(streamId, periodStart, periodEnd);
            remainingCount = remainingCount != null ? remainingCount : 0;
            
            Map<String, Object> dataItem = new LinkedHashMap<>();
            dataItem.put("x", periodList.get(5 - i));
            dataItem.put("resolved", resolvedCount);
            dataItem.put("remaining", remainingCount);
            result.add(dataItem);
        }

        log.info("获取碰撞检测趋势成功, streamId: {}, type: {}", streamId, type);
        return result;
    }

    @Override
    public List<Map<String, Object>> getLODComplianceScore(String streamId, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> periodList = getPeriodList(type);
        
        // 遍历最近6周或6个月
        for (int i = 5; i >= 0; i--) {
            Date periodStart = getPeriodStartDate(type, i);
            Date periodEnd = getPeriodEndDate(type, i);
            
            // 获取该周期内的LOI/LOD得分
            Map<String, Object> scoreData = dashboardMapper.getLODScoreByPeriod(streamId, periodStart, periodEnd);
            
            int loiScore = 0;
            int lodScore = 0;
            
            if (scoreData != null) {
                if (scoreData.get("loi_score") != null) {
                    loiScore = ((Number) scoreData.get("loi_score")).intValue();
                }
                if (scoreData.get("lod_score") != null) {
                    lodScore = ((Number) scoreData.get("lod_score")).intValue();
                }
            }
            
            Map<String, Object> dataItem = new LinkedHashMap<>();
            dataItem.put("x", periodList.get(5 - i));
            dataItem.put("loiScore", loiScore);
            dataItem.put("lodScore", lodScore);
            result.add(dataItem);
        }

        log.info("获取LOD合规得分成功, streamId: {}, type: {}", streamId, type);
        return result;
    }

    @Override
    public List<Map<String, Object>> getPendingIssueCategories(String streamId, String type) {
        int days = getDaysByType(type);
        Date startDate = getCurrentPeriodStartDate(days);
        Date endDate = getCurrentPeriodEndDate();

        // 获取碰撞干扰数量（sp_crash_detection中status为1）
        Integer crashCount = dashboardMapper.countUnresolvedCrashDetection(streamId, startDate, endDate);
        crashCount = crashCount != null ? crashCount : 0;

        // 获取不符合审查规范数量（sp_revit_lod中value_not_pass不为0）
        Integer reviewNotPassCount = dashboardMapper.countReviewNotPass(streamId, startDate, endDate);
        reviewNotPassCount = reviewNotPassCount != null ? reviewNotPassCount : 0;

        // 获取属性参数缺失/错误数量（暂时设为0，需要明确数据来源）
        int propertyErrorCount = 0;

        // 获取人工问题批注数量
        Integer commentCount = dashboardMapper.countPendingComments(streamId, startDate, endDate);
        commentCount = commentCount != null ? commentCount : 0;

        // 计算总数
        int totalCount = crashCount + reviewNotPassCount + propertyErrorCount + commentCount;

        // 定义固定的4个分类，按照指定顺序
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Object> category1 = new LinkedHashMap<>();
        category1.put("name", "碰撞干扰");
        category1.put("color", "#9566E5");
        category1.put("count", crashCount);
        // 计算百分比，如果总数为0，则百分比为0（返回数字，不带%符号）
        if (totalCount > 0) {
            category1.put("num", (int)Math.round(crashCount * 100.0 / totalCount));
        } else {
            category1.put("num", 0);
        }
        result.add(category1);

        Map<String, Object> category2 = new LinkedHashMap<>();
        category2.put("name", "不符合审查规范");
        category2.put("color", "#F56C6C");
        category2.put("count", reviewNotPassCount);
        if (totalCount > 0) {
            category2.put("num", (int)Math.round(reviewNotPassCount * 100.0 / totalCount));
        } else {
            category2.put("num", 0);
        }
        result.add(category2);

        Map<String, Object> category3 = new LinkedHashMap<>();
        category3.put("name", "属性参数缺失/错误");
        category3.put("color", "#E6A23C");
        category3.put("count", propertyErrorCount);
        if (totalCount > 0) {
            category3.put("num", (int)Math.round(propertyErrorCount * 100.0 / totalCount));
        } else {
            category3.put("num", 0);
        }
        result.add(category3);

        Map<String, Object> category4 = new LinkedHashMap<>();
        category4.put("name", "人工问题批注");
        category4.put("color", "#4080FF");
        category4.put("count", commentCount);
        if (totalCount > 0) {
            category4.put("num", (int)Math.round(commentCount * 100.0 / totalCount));
        } else {
            category4.put("num", 0);
        }
        result.add(category4);

        log.info("获取待处理问题分类成功, streamId: {}, type: {}, 总数: {}, 碰撞干扰: {}({}), 不符合规范: {}({}), 属性错误: {}({}), 人工批注: {}({})",
                streamId, type, totalCount,
                crashCount, category1.get("num"),
                reviewNotPassCount, category2.get("num"),
                propertyErrorCount, category3.get("num"),
                commentCount, category4.get("num"));
        return result;
    }

    @Override
    public List<Map<String, Object>> getTeamSyncActivity(String streamId, String type) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> periodList = getPeriodList(type);
        
        // 遍历最近6周或6个月
        for (int i = 5; i >= 0; i--) {
            Date periodStart = getPeriodStartDate(type, i);
            Date periodEnd = getPeriodEndDate(type, i);
            
            // A: commits表中的记录数
            Integer commitsCount = dashboardMapper.countCommitsByPeriod(streamId, periodStart, periodEnd);
            commitsCount = commitsCount != null ? commitsCount : 0;
            
            // B: sp_template_version表中的记录数
            Integer templateVersionCount = dashboardMapper.countTemplateVersionByPeriod(streamId, periodStart, periodEnd);
            templateVersionCount = templateVersionCount != null ? templateVersionCount : 0;
            
            // 总数 = A + B
            int totalCount = commitsCount + templateVersionCount;
            
            Map<String, Object> dataItem = new LinkedHashMap<>();
            dataItem.put("x", periodList.get(5 - i));
            dataItem.put("count", totalCount);
            result.add(dataItem);
        }

        log.info("获取团队数据同步活跃度成功, streamId: {}, type: {}", streamId, type);
        return result;
    }

    @Override
    public List<Map<String, Object>> getHighFrequencyChangeModels(String streamId, String type) {
        int days = getDaysByType(type);
        Date startDate = getCurrentPeriodStartDate(days);
        Date endDate = getCurrentPeriodEndDate();

        List<Map<String, Object>> modelData = dashboardMapper.getHighFrequencyChangeModels(streamId, startDate, endDate);

        List<Map<String, Object>> result = new ArrayList<>();

        // 预览URL配置，可根据实际环境配置
        String previewBaseUrl = "http://10.5.57.107:8100/preview";

        if (modelData != null) {
            for (Map<String, Object> item : modelData) {
                Map<String, Object> model = new LinkedHashMap<>();
                
                String streamIdValue = String.valueOf(item.get("stream_id"));
                String branchIdValue = String.valueOf(item.get("branch_id"));
                String latestCommitId = item.get("latest_commit_id") != null ?
                        String.valueOf(item.get("latest_commit_id")) : "";
                
                model.put("streamId", streamIdValue);
                model.put("branchId", branchIdValue);
                model.put("name", item.get("name"));
                
                // 构建预览URL
                String previewUrl = previewBaseUrl + "/" + streamIdValue + "/commits/" + latestCommitId;
                model.put("previewUrl", previewUrl);
                
                // 变更次数
                Object changeNumObj = item.get("change_num");
                model.put("changeNum", changeNumObj instanceof Number ? ((Number) changeNumObj).intValue() : 0);
                
                // 未解决碰撞数
                Object unresolveObj = item.get("unresolve");
                model.put("unresolve", unresolveObj instanceof Number ? ((Number) unresolveObj).intValue() : 0);
                
                result.add(model);
            }
        }

        log.info("获取高频变更模型成功, streamId: {}, type: {}, 记录数: {}", streamId, type, result.size());
        return result;
    }
}
