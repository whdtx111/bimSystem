package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.Ebs;

import java.util.List;

public interface EbsMapper extends BaseMapper<Ebs> {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Ebs getById(String id);

    /**
     * 获取列表
     * @return
     */
    List<Ebs> getAllEbs();

    /**
     * 筛选器
     * @param type
     * @param source
     * @param ebsStatus
     * @param auth
     * @param ebsName
     * @param ebsCode
     * @return
     */
    Page<Ebs> filterEbs(@Param("ebsCode") String ebsCode,
                        @Param("ebsName") String ebsName,
                        @Param("type") String type,
                        @Param("source") String source,
                        @Param("auth") String auth,
                        @Param("ebsStatus") Integer ebsStatus);

    /**
     * 新增EBS
     * @param ebs
     * @return
     */
    boolean addEbs(Ebs ebs);

    /**
     * 删除EBS
     * @param id
     * @return
     */
    boolean deleteEbs(String id);

    /**
     * 修改EBS
     * @param ebs
     * @return
     */
    boolean updateEbs(Ebs ebs);

    boolean updateEbsStatus(String id);
}
