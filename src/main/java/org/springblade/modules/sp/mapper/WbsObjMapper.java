package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.Ebs;
import org.springblade.modules.sp.entity.WbsObj;

import java.util.List;
public interface WbsObjMapper extends BaseMapper<WbsObj> {

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
