package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springblade.modules.sp.entity.Label;

import java.util.List;

/**
 * 标签Mapper接口
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    /**
     * 根据id查询单个标签
     */
    @Select("SELECT * FROM sp_label WHERE id = #{id}")
    Label getLabelById(@Param("id") String id);

    /**
     * 根据streamId查询标签列表
     */
    @Select("SELECT * FROM sp_label WHERE stream_id = #{streamId} ORDER BY created_time DESC")
    List<Label> getLabelsByStreamId(@Param("streamId") String streamId);

    /**
     * 根据branchId查询标签列表
     */
    @Select("SELECT * FROM sp_label WHERE branch_id = #{branchId} ORDER BY created_time DESC")
    List<Label> getLabelsByBranchId(@Param("branchId") String branchId);

    /**
     * 根据commitId查询标签列表
     */
    @Select("SELECT * FROM sp_label WHERE commit_id = #{commitId} ORDER BY created_time DESC")
    List<Label> getLabelsByCommitId(@Param("commitId") String commitId);

    /**
     * 根据streamId、branchId和commitId查询标签列表
     */
    List<Label> getLabelsByStreamBranchCommit(@Param("streamId") String streamId, 
                                               @Param("branchId") String branchId, 
                                               @Param("commitId") String commitId);

    /**
     * 根据type查询标签列表
     */
    @Select("SELECT * FROM sp_label WHERE type = #{type} ORDER BY created_time DESC")
    List<Label> getLabelsByType(@Param("type") String type);

    /**
     * 根据status查询标签列表
     */
    @Select("SELECT * FROM sp_label WHERE status = #{status} ORDER BY created_time DESC")
    List<Label> getLabelsByStatus(@Param("status") Integer status);

    /**
     * 查询所有标签
     */
    @Select("SELECT * FROM sp_label ORDER BY created_time DESC")
    List<Label> getAllLabels();

    /**
     * 插入一个新的标签
     */
    @Insert("INSERT INTO sp_label (id, stream_id, branch_id, commit_id, type, text, size, color, status, img_index, position, created_time, updated_time) " +
            "VALUES (#{id}, #{streamId}, #{branchId}, #{commitId}, #{type}, #{text}, #{size}, #{color}, #{status}, #{imgIndex}, #{position}, #{createdTime}, #{updatedTime})")
    int insertLabel(Label label);

    /**
     * 更新标签
     */
    int updateLabelSelective(Label label);

    /**
     * 根据id删除标签
     */
    @Delete("DELETE FROM sp_label WHERE id = #{id}")
    int deleteLabel(@Param("id") String id);

    /**
     * 根据streamId删除标签
     */
    @Delete("DELETE FROM sp_label WHERE stream_id = #{streamId}")
    int deleteLabelsByStreamId(@Param("streamId") String streamId);

    /**
     * 根据branchId删除标签
     */
    @Delete("DELETE FROM sp_label WHERE branch_id = #{branchId}")
    int deleteLabelsByBranchId(@Param("branchId") String branchId);

    /**
     * 根据commitId删除标签
     */
    @Delete("DELETE FROM sp_label WHERE commit_id = #{commitId}")
    int deleteLabelsByCommitId(@Param("commitId") String commitId);

    /**
     * 批量插入标签
     */
    int batchInsertLabels(@Param("labels") List<Label> labels);

    /**
     * 统计标签数量
     */
    @Select("SELECT COUNT(*) FROM sp_label")
    int countLabels();

    /**
     * 根据streamId统计标签数量
     */
    @Select("SELECT COUNT(*) FROM sp_label WHERE stream_id = #{streamId}")
    int countLabelsByStreamId(@Param("streamId") String streamId);
}
