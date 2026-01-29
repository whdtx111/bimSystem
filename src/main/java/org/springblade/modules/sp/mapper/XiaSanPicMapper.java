package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.XiaSanPic;

import java.util.List;
@Mapper
public interface XiaSanPicMapper extends BaseMapper<XiaSanPic> {

    List<XiaSanPic> searchFilter(String streamId,String commitId,String objectId,String elementId,String checkStatus);


    XiaSanPic getByName(String name, String streamId, String elementId);

    XiaSanPic getById(String id);

    boolean addXiaSanPic(XiaSanPic xiaSanPic);

    boolean deleteXiaSanPicById(String id);

    boolean updateXiaSanPic(XiaSanPic xiaSanPic);

}
