package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Ebs;

import java.util.List;

public interface EbsService extends BaseService<Ebs> {
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
    Page<Ebs> filterEbs(String ebsCode,
                        String ebsName,
                        String type,
                        String source,
                        String auth,
                        Integer ebsStatus, Integer pageSize, Integer currentPage);

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
