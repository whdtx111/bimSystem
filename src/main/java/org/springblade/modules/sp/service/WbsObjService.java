package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.WbsObj;

import java.util.List;

public interface WbsObjService extends BaseService<WbsObj> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    WbsObj getById(String id);
    /**
     * link
     * @param wbsId
     * @param objectId
     * @return
     */
    WbsObj getLink(String wbsId,String objectId);
    /**
     * 获取列表
     * @return
     */
    List<WbsObj> getAllWbsObj();

    /**
     * 新增
     * @param
     * @return
     */
    boolean addWbsObj(WbsObj wbsObj);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteWbsObj(String id,String modifyUser);

    /**
     * 修改
     * @param
     * @return
     */
    boolean updateWbsObj(WbsObj wbsObj);
}
