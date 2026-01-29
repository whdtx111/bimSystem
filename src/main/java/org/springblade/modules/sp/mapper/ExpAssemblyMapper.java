package org.springblade.modules.sp.mapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.ExpAssembly;

@Mapper
public interface ExpAssemblyMapper extends BaseMapper<ExpAssembly> {

    /**
     * 根据streamId和commitId查询assembly数据
     * @param streamId
     * @param commitId
     * @return
     */
    JSONArray getAssemblyByStreamAndCommit(@Param("streamId") String streamId, @Param("commitId") String commitId);


}
