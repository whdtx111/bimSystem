package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.dto.StreamColorDataDTO;
import org.springblade.modules.sp.entity.StreamColorData;
import org.springblade.modules.sp.mapper.StreamColorDataMapper;
import org.springblade.modules.sp.service.IStreamColorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stream颜色数据服务实现类
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Service
@DS("postgresql")
public class StreamColorDataServiceImpl extends BaseServiceImpl<StreamColorDataMapper, StreamColorData> 
        implements IStreamColorDataService {

    private static final Logger logger = LogManager.getLogger(StreamColorDataServiceImpl.class);

    @Autowired
    private StreamColorDataMapper streamColorDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveColorData(StreamColorDataDTO dto) {
        try {
            if (dto == null || StringUtils.isBlank(dto.getStreamId()) || StringUtils.isBlank(dto.getCommitId())) {
                logger.error("保存失败：streamId或commitId为空");
                return false;
            }

            if (dto.getData() == null || dto.getData().isEmpty()) {
                logger.error("保存失败：data数据为空");
                return false;
            }

            List<StreamColorData> insertList = new ArrayList<>();
            int updateCount = 0;
            
            for (StreamColorDataDTO.ColorDataItem item : dto.getData()) {
                if (StringUtils.isBlank(item.getNodeId())) {
                    continue;
                }
                
                // 查询是否已存在相同的streamId/commitId/nodeId记录
                StreamColorData existingData = streamColorDataMapper.selectByStreamCommitAndNode(
                    dto.getStreamId(),
                    dto.getCommitId(),
                    item.getNodeId()
                );
                
                if (existingData != null) {
                    // 存在则更新color值
                    int updated = streamColorDataMapper.updateColor(existingData.getId(), item.getColor());
                    if (updated > 0) {
                        updateCount++;
                    }
                } else {
                    // 不存在则准备插入
                    StreamColorData entity = new StreamColorData(
                        dto.getStreamId(),
                        dto.getCommitId(),
                        item.getNodeId(),
                        item.getColor()
                    );
                    insertList.add(entity);
                }
            }

            // 批量插入新记录
            int insertCount = 0;
            if (!insertList.isEmpty()) {
                insertCount = streamColorDataMapper.batchInsert(insertList);
            }

            logger.info("保存Stream颜色数据成功，streamId: {}, commitId: {}, 插入: {}, 更新: {}", 
                    dto.getStreamId(), dto.getCommitId(), insertCount, updateCount);
            
            return (insertCount + updateCount) > 0;
        } catch (Exception e) {
            logger.error("保存Stream颜色数据失败", e);
            throw new RuntimeException("保存Stream颜色数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StreamColorData> getByStreamAndCommit(String streamId, String commitId) {
        try {
            if (StringUtils.isBlank(streamId) || StringUtils.isBlank(commitId)) {
                logger.error("查询失败：streamId或commitId为空");
                return new ArrayList<>();
            }
            
            List<StreamColorData> dataList = streamColorDataMapper.selectByStreamAndCommit(streamId, commitId);
            logger.info("查询Stream颜色数据成功，streamId: {}, commitId: {}, 数据量: {}", 
                    streamId, commitId, dataList != null ? dataList.size() : 0);
            
            return dataList != null ? dataList : new ArrayList<>();
        } catch (Exception e) {
            logger.error("查询Stream颜色数据失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByStreamAndCommit(String streamId, String commitId) {
        try {
            if (StringUtils.isBlank(streamId) || StringUtils.isBlank(commitId)) {
                logger.error("删除失败：streamId或commitId为空");
                return false;
            }
            
            int result = streamColorDataMapper.deleteByStreamAndCommit(streamId, commitId);
            logger.info("删除Stream颜色数据成功，streamId: {}, commitId: {}, 删除数量: {}", 
                    streamId, commitId, result);
            
            return result > 0;
        } catch (Exception e) {
            logger.error("删除Stream颜色数据失败", e);
            throw new RuntimeException("删除Stream颜色数据失败: " + e.getMessage(), e);
        }
    }

}
