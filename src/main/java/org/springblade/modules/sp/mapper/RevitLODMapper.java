package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.RevitLOD;

import java.util.List;

@Mapper
public interface RevitLODMapper extends BaseMapper<RevitLOD> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RevitLOD getById(@Param("id") String id);

    /**
     * 根据checkId查询
     * @param checkId
     * @return
     */
    RevitLOD getByCheckId(@Param("checkId") String checkId);

    /**
     * 根据streamId、branchId、commitId查询列表
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    RevitLOD getByStreamBranchCommit(@Param("streamId") String streamId,
                                           @Param("branchId") String branchId, 
                                           @Param("commitId") String commitId);

    /**
     * 根据streamId、branchId、commitId、checkId查询记录
     * @param streamId
     * @param branchId
     * @param commitId
     * @param checkId
     * @return
     */
    RevitLOD getByStreamBranchCommitAndCheckId(@Param("streamId") String streamId,
                                              @Param("branchId") String branchId, 
                                              @Param("commitId") String commitId,
                                              @Param("checkId") String checkId);

    /**
     * 根据streamId、branchId、commitId查询所有记录列表
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    List<RevitLOD> getListByStreamBranchCommit(@Param("streamId") String streamId,
                                              @Param("branchId") String branchId, 
                                              @Param("commitId") String commitId);

    /**
     * 根据streamId、branchId查询最新的记录列表（不需要commitId）
     * @param streamId
     * @param branchId
     * @return
     */
    List<RevitLOD> getLatestListByStreamBranch(@Param("streamId") String streamId,
                                               @Param("branchId") String branchId);

    /**
     * 根据streamId查询列表
     * @param streamId
     * @return
     */
    List<RevitLOD> getByStreamId(@Param("streamId") String streamId);

    /**
     * 新增
     * @param revitLOD
     * @return
     */
    int addRevitLOD(RevitLOD revitLOD);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据checkId删除
     * @param checkId
     * @return
     */
    int deleteByCheckId(@Param("checkId") String checkId);

    /**
     * 更新
     * @param revitLOD
     * @return
     */
    int updateRevitLOD(RevitLOD revitLOD);

    /**
     * 获取所有数据
     * @return
     */
    List<RevitLOD> getAllRevitLOD();
}
