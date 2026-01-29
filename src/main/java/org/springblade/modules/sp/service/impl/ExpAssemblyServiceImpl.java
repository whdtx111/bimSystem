package org.springblade.modules.sp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.sp.entity.ExpAssembly;
import org.springblade.modules.sp.mapper.ExpAssemblyMapper;
import org.springblade.modules.sp.service.ExpAssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpAssemblyServiceImpl extends ServiceImpl<ExpAssemblyMapper, ExpAssembly> implements ExpAssemblyService {

    @Autowired
    private ExpAssemblyMapper expAssemblyMapper;

    @Override
    public JSONArray getAssemblyByStreamAndCommit(String streamId, String commitId) {
        return expAssemblyMapper.getAssemblyByStreamAndCommit(streamId, commitId);
    }
}
