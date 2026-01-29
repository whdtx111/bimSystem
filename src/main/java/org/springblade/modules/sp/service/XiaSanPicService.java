package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.XiaSanPic;

import java.util.List;

public interface XiaSanPicService extends BaseService<XiaSanPic> {

    List<XiaSanPic> searchFilter(String streamId,String commitId,String objectId,String elementId,String checkStatus);

    XiaSanPic getByName(String name, String streamId, String elementId);

    XiaSanPic getById(String id);

    boolean addXiaSanPic(XiaSanPic xiaSanPic);

    boolean deleteXiaSanPicById(String id);

    boolean updateXiaSanPic(XiaSanPic xiaSanPic);
}
