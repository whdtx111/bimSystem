package org.springblade.modules.sp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.FieldResource;

import java.util.List;


public interface FieldResourceService extends IService<FieldResource> {

    FieldResource getById(String id);

    List<FieldResource> getByTag(String tag);

    List<FieldResource> getAll();

    boolean addFieldResource(FieldResource fieldResource);

    boolean updateFieldResource(FieldResource fieldResource);

    boolean deleteById(String id);

}
