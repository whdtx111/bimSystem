package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.Wbs;

import java.util.List;
public interface WbsMapper extends BaseMapper<Wbs> {

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

    Page<Wbs> filterWbs(@Param("wbsCode") String wbsCode,
                        @Param("wbsName") String wbsName,
                        @Param("type") String type,
                        @Param("source") String source,
                        @Param("auth") String auth,
                        @Param("wbsStatus") Integer wbsStatus);

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
