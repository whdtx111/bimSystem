package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Wbs;

import java.util.List;

public interface WbsService extends BaseService<Wbs> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Wbs getById(String id);

    /**
     * 获取列表
     * @return
     */
    List<Wbs> getAllWbs();


    Page<Wbs> filterWbs(String wbsCode,
                       String wbsName,
                        String type,
                         String source,
                       String auth,
                         Integer wbsStatus,Integer pageSize, Integer currentPage);

    /**
     * 新增Wbs
     * @param
     * @return
     */
    boolean addWbs(Wbs wbs);

    boolean insertWbsList(List<Wbs> wbsList);

    /**
     * 删除Wbs
     * @param id
     * @return
     */
    boolean deleteWbs(String id);

    /**
     * 修改Wbs
     * @param
     * @return
     */
    boolean updateWbs(Wbs wbs);

    boolean updateWbsStatus(String id);
}
