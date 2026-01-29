package org.springblade.modules.sp.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.ExpInstances;
import org.springblade.modules.sp.vo.NewBimListTreeVO;

import java.util.List;
@Mapper
public interface ExpInstancesMapper extends BaseMapper<ExpInstances> {

    /**
     * 根据streamId和id查询
     * @param streamId
     * @return
     */
    ExpInstances getExpInstances(String streamId,String objId,String commitId);

    List<ExpInstances> getExpInstancesFilter(String streamId,String category,String family,String type,String commitId);

    ExpInstances searchExpInstances(String streamId, String elementId, String commitId);

    /**
     * 添加
     *
     * @param expInstances
     * @return
     */
    boolean addExpInstances(ExpInstances expInstances);

    boolean updateExpInstances(ExpInstances expInstances);

    /**
     * delete
     * @param commitId
     * @return
     */
    boolean deleteExpInstances(String commitId);

    boolean deleteExpInstancesById(String Id);

    /**
     * 根据objIds数组获取实例数据
     * @param objIds objId数组
     * @return 实例列表
     */
    List<ExpInstances> getExpInstancesByObjIds(String streamId,String branchId,@Param("objIds") String[] objIds);

    List<NewBimListTreeVO> BimListTreeNew(String streamId,String commitId);

    List<ExpInstances> getAllExpInstances(String streamId,String commitId);

    List<ExpInstances> getExpInstancesByElements(String streamId,String[] elementId,String commitId);

    ExpInstances getParameterByElementId(String elementId,String branchId,String category);

    /**
     * 根据elementId、streamId和commitId查询实例
     * @param elementId
     * @param streamId
     * @param commitId
     * @return
     */
    ExpInstances getInstanceByElementIdAndStreamAndCommit(@Param("elementId") String elementId,
                                                          @Param("streamId") String streamId,
                                                          @Param("commitId") String commitId);
}
