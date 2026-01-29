package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.Label;
import org.springblade.modules.sp.mapper.LabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 标签服务类
 */
@Service
@DS("postgresql")
@Slf4j
public class LabelService {

    @Autowired
    private LabelMapper labelMapper;

    /**
     * 根据id查询标签
     */
    public Label getLabelById(String id) {
        try {
            return labelMapper.getLabelById(id);
        } catch (Exception e) {
            log.error("根据id查询标签失败: id={}", id, e);
            throw new RuntimeException("查询标签失败", e);
        }
    }

    /**
     * 根据streamId查询标签列表
     */
    public List<Label> getLabelsByStreamId(String streamId) {
        try {
            return labelMapper.getLabelsByStreamId(streamId);
        } catch (Exception e) {
            log.error("根据streamId查询标签列表失败: streamId={}", streamId, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 根据branchId查询标签列表
     */
    public List<Label> getLabelsByBranchId(String branchId) {
        try {
            return labelMapper.getLabelsByBranchId(branchId);
        } catch (Exception e) {
            log.error("根据branchId查询标签列表失败: branchId={}", branchId, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 根据commitId查询标签列表
     */
    public List<Label> getLabelsByCommitId(String commitId) {
        try {
            return labelMapper.getLabelsByCommitId(commitId);
        } catch (Exception e) {
            log.error("根据commitId查询标签列表失败: commitId={}", commitId, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 根据streamId、branchId和commitId查询标签列表
     */
    public List<Label> getLabelsByStreamBranchCommit(String streamId, String branchId, String commitId) {
        try {
            return labelMapper.getLabelsByStreamBranchCommit(streamId, branchId, commitId);
        } catch (Exception e) {
            log.error("根据streamId、branchId和commitId查询标签列表失败: streamId={}, branchId={}, commitId={}", 
                      streamId, branchId, commitId, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 根据type查询标签列表
     */
    public List<Label> getLabelsByType(String type) {
        try {
            return labelMapper.getLabelsByType(type);
        } catch (Exception e) {
            log.error("根据type查询标签列表失败: type={}", type, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 根据status查询标签列表
     */
    public List<Label> getLabelsByStatus(Integer status) {
        try {
            return labelMapper.getLabelsByStatus(status);
        } catch (Exception e) {
            log.error("根据status查询标签列表失败: status={}", status, e);
            throw new RuntimeException("查询标签列表失败", e);
        }
    }

    /**
     * 查询所有标签
     */
    public List<Label> getAllLabels() {
        try {
            return labelMapper.getAllLabels();
        } catch (Exception e) {
            log.error("查询所有标签失败", e);
            throw new RuntimeException("查询所有标签失败", e);
        }
    }

    /**
     * 新增标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addLabel(Label label) {
        try {
            if (label.getId() == null || label.getId().isEmpty()) {
                label.setId(java.util.UUID.randomUUID().toString());
            }
            if (label.getStatus() == null) {
                label.setStatus(0);
            }
            if (label.getCreatedTime() == null) {
                label.setCreatedTime(new Date());
            }
            if (label.getUpdatedTime() == null) {
                label.setUpdatedTime(new Date());
            }
            int result = labelMapper.insertLabel(label);
            log.info("新增标签成功: id={}", label.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("新增标签失败: label={}", label, e);
            throw new RuntimeException("新增标签失败", e);
        }
    }

    /**
     * 更新标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLabel(Label label) {
        try {
            label.setUpdatedTime(new Date());
            int result = labelMapper.updateLabelSelective(label);
            log.info("更新标签成功: id={}", label.getId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新标签失败: label={}", label, e);
            throw new RuntimeException("更新标签失败", e);
        }
    }

    /**
     * 根据id删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLabel(String id) {
        try {
            int result = labelMapper.deleteLabel(id);
            log.info("删除标签成功: id={}", id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除标签失败: id={}", id, e);
            throw new RuntimeException("删除标签失败", e);
        }
    }

    /**
     * 根据streamId删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLabelsByStreamId(String streamId) {
        try {
            int result = labelMapper.deleteLabelsByStreamId(streamId);
            log.info("根据streamId删除标签成功: streamId={}, 删除数量={}", streamId, result);
            return result > 0;
        } catch (Exception e) {
            log.error("根据streamId删除标签失败: streamId={}", streamId, e);
            throw new RuntimeException("删除标签失败", e);
        }
    }

    /**
     * 根据branchId删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLabelsByBranchId(String branchId) {
        try {
            int result = labelMapper.deleteLabelsByBranchId(branchId);
            log.info("根据branchId删除标签成功: branchId={}, 删除数量={}", branchId, result);
            return result > 0;
        } catch (Exception e) {
            log.error("根据branchId删除标签失败: branchId={}", branchId, e);
            throw new RuntimeException("删除标签失败", e);
        }
    }

    /**
     * 根据commitId删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLabelsByCommitId(String commitId) {
        try {
            int result = labelMapper.deleteLabelsByCommitId(commitId);
            log.info("根据commitId删除标签成功: commitId={}, 删除数量={}", commitId, result);
            return result > 0;
        } catch (Exception e) {
            log.error("根据commitId删除标签失败: commitId={}", commitId, e);
            throw new RuntimeException("删除标签失败", e);
        }
    }

    /**
     * 批量插入标签
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsertLabels(List<Label> labels) {
        try {
            if (labels == null || labels.isEmpty()) {
                log.warn("批量插入标签失败: 标签列表为空");
                return false;
            }
            // 确保每个标签都有必要的字段
            for (Label label : labels) {
                if (label.getId() == null || label.getId().isEmpty()) {
                    label.setId(java.util.UUID.randomUUID().toString());
                }
                if (label.getStatus() == null) {
                    label.setStatus(0);
                }
                if (label.getCreatedTime() == null) {
                    label.setCreatedTime(new Date());
                }
                if (label.getUpdatedTime() == null) {
                    label.setUpdatedTime(new Date());
                }
            }
            int result = labelMapper.batchInsertLabels(labels);
            log.info("批量插入标签成功: 数量={}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量插入标签失败: labels.size={}", labels != null ? labels.size() : 0, e);
            throw new RuntimeException("批量插入标签失败", e);
        }
    }

    /**
     * 统计标签数量
     */
    public int countLabels() {
        try {
            return labelMapper.countLabels();
        } catch (Exception e) {
            log.error("统计标签数量失败", e);
            throw new RuntimeException("统计标签数量失败", e);
        }
    }

    /**
     * 根据streamId统计标签数量
     */
    public int countLabelsByStreamId(String streamId) {
        try {
            return labelMapper.countLabelsByStreamId(streamId);
        } catch (Exception e) {
            log.error("根据streamId统计标签数量失败: streamId={}", streamId, e);
            throw new RuntimeException("统计标签数量失败", e);
        }
    }
}
