package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.CrashDetection;

import java.util.List;

/**
 * 碰撞检测Mapper接口
 * 
 * @author Yi
 * @since 2024-11-12
 */
@Mapper
public interface CrashDetectionMapper extends BaseMapper<CrashDetection> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CrashDetection getById(@Param("id") String id);

    /**
     * 根据streamId、branchId、commitId查询
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    CrashDetection getByStreamBranchCommit(@Param("streamId") String streamId,
                                          @Param("branchId") String branchId, 
                                          @Param("commitId") String commitId);

    /**
     * 根据streamId查询列表
     * @param streamId
     * @return
     */
    List<CrashDetection> getByStreamId(@Param("streamId") String streamId);

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
    int addCrashDetection(CrashDetection crashDetection);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据streamId、branchId、commitId删除
     * @param streamId
     * @param branchId
     * @param commitId
     * @return
     */
    int deleteByStreamBranchCommit(@Param("streamId") String streamId,
                                   @Param("branchId") String branchId, 
                                   @Param("commitId") String commitId);

    /**
     * 更新
     * @param crashDetection
     * @return
     */
    int updateCrashDetection(CrashDetection crashDetection);
}
