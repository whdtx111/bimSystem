package org.springblade.modules.sp.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.tool.api.R;
import org.springblade.modules.sp.entity.ElementParameter;
import org.springblade.modules.sp.entity.ExpInstances;
import org.springblade.modules.sp.vo.BimListTreeVO;
import org.springblade.modules.sp.vo.ExpInstancesDetailVO;
import org.springblade.modules.sp.vo.NewBimListTreeVO;
import org.springblade.modules.sp.vo.ObjParameterVO;

import java.util.List;

public interface ExpInstancesService extends BaseService<ExpInstances>{




    /**
     * 根据streamId和id查询
     * @param streamId
     * @param
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

    /**
     * delete
     * @param commitId
     * @return
     */
    boolean deleteExpInstances(String commitId);

    boolean deleteExpInstancesById(String Id);

    boolean updateExpInstances(ExpInstances expInstances);

    /**
     * 根据objIds数组获取实例详细数据
     * @param objIds objId数组
     * @return 格式化后的实例详细信息列表
     */
    List<ExpInstancesDetailVO> getExpInstancesDetailByObjIds(String streamId,String branchId,String[] objIds);

    List<NewBimListTreeVO> BimListTreeNew(String streamId,String commitId);

    List<ExpInstances> getAllExpInstances(String streamId,String commitId);

    List<ExpInstances> getExpInstancesByElements(String streamId,String[] elementId,String commitId);

    R<List<ElementParameter>> getParameterByElementId(String elementId, String branchId,String name,String category);

    /**
     * 根据elementId、streamId和commitId获取实例
     * @param elementId
     * @param streamId
     * @param commitId
     * @return
     */
    ExpInstances getInstanceByElementIdAndStreamAndCommit(String elementId, String streamId, String commitId);

}
