package org.springblade.modules.sp.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Encoding;
import org.springblade.modules.sp.entity.ExpAssembly;

import java.util.List;

public interface ExpAssemblyService extends IService<ExpAssembly> {

    /**
     * 根据streamId和commitId获取assembly数据
     * @param streamId
     * @param commitId
     * @return
     */
    JSONArray getAssemblyByStreamAndCommit(String streamId, String commitId);
}
