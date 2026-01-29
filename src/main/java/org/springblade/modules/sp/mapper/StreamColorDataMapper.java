package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.StreamColorData;

import java.util.List;

/**
 * Stream颜色数据Mapper接口
 *
 * @author auto-generated
 * @since 2026-01-05
 */
@Mapper
public interface StreamColorDataMapper extends BaseMapper<StreamColorData> {

    /**
     * 根据streamId和commitId查询数据列表
     *
     * @param streamId StreamID
     * @param commitId CommitID
     * @return 数据列表
     */
    List<StreamColorData> selectByStreamAndCommit(@Param("streamId") String streamId, 
                                                   @Param("commitId") String commitId);

    /**
     * 根据streamId、commitId和nodeId查询单条记录
     *
     * @param streamId StreamID
     * @param commitId CommitID
     * @param nodeId NodeID
     * @return 单条记录
     */
    StreamColorData selectByStreamCommitAndNode(@Param("streamId") String streamId,
                                                @Param("commitId") String commitId,
                                                @Param("nodeId") String nodeId);

    /**
     * 批量插入数据
     *
     * @param dataList 数据列表
     * @return 插入的行数
     */
    int batchInsert(@Param("dataList") List<StreamColorData> dataList);

    /**
     * 更新颜色值
     *
     * @param id 主键ID
     * @param color 颜色值
     * @return 更新的行数
     */
    int updateColor(@Param("id") String id, @Param("color") String color);

    /**
     * 根据streamId和commitId删除数据
     *
     * @param streamId StreamID
     * @param commitId CommitID
     * @return 删除的行数
     */
    int deleteByStreamAndCommit(@Param("streamId") String streamId, 
                                 @Param("commitId") String commitId);
}
