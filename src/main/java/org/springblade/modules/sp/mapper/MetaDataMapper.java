package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.MetaData;
import org.springblade.modules.sp.entity.Wbs;

import java.util.List;

public interface MetaDataMapper extends BaseMapper<MetaData> {

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
     * MetaData
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
