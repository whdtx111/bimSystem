package org.springblade.modules.sp.service;


import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.MetaData;

import java.util.List;

public interface MetaService extends BaseService<MetaData> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    MetaData getById(String id);

    /**
     * 获取列表
     * @return
     */
    List<MetaData> getAllMetaData();

    /**
     * 新增
     * @param metaData
     * @return
     */
    boolean addMetaData(MetaData metaData);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean deleteMetaData(String id);

    /**
     * 修改
     * @param metaData
     * @return
     */
    boolean updateMetaData(MetaData metaData);
}
