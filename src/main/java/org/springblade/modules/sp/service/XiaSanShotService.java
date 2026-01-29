package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.XiaSanShot;

import java.util.List;

public interface XiaSanShotService extends BaseService<XiaSanShot> {

    XiaSanShot getById(String id);

    XiaSanShot getByElementId(String streamId,String elementId);

    List<String> getcheckStatus(String streamId,String commitId);

    int countByCommitId(String commitId,String streamId,String checkStatus);

    boolean addXiaSanShot(XiaSanShot xiaSanShot);

    boolean deleteXiaSanShotById(String id);

    boolean updateXiaSanShot(XiaSanShot xiaSanShot);

    List<XiaSanShot> searchFilter(String checkStatus,String streamId,String commitId,String elementId,String objectId);
}
