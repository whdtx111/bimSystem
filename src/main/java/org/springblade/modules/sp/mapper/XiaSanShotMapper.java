package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.XiaSanShot;

import java.util.List;
@Mapper
public interface XiaSanShotMapper extends BaseMapper<XiaSanShot> {

    XiaSanShot getById(String id);

    XiaSanShot getByElementId(String streamId,String elementId);

    List<String> getcheckStatus(String streamId,String commitId);

    int countByCommitId(String commitId,String streamId,String checkStatus);

    boolean addXiaSanShot(XiaSanShot xiaSanShot);

    boolean deleteXiaSanShotById(String id);

    boolean updateXiaSanShot(XiaSanShot xiaSanShot);

    List<XiaSanShot> searchFilter(String checkStatus,String streamId,String commitId,String elementId,String objectId);
}
