package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.RevitFiles;
import org.springblade.modules.sp.mapper.RevitFilesMapper;
import org.springblade.modules.sp.service.RevitFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class RevitFilesServiceImpl extends BaseServiceImpl<RevitFilesMapper, RevitFiles> implements RevitFilesService {

    @Autowired
    private RevitFilesMapper revitFilesMapper;


    @Override
    public RevitFiles getById(String id) {
        return revitFilesMapper.getById(id);
    }

    @Override
    public RevitFiles getByName(String streamId, String branchId, String commitId,String name) {
        return revitFilesMapper.getByName(streamId, branchId, commitId,name);
    }
    @Override
    public List<RevitFiles> searchFilter(String streamId, String branchId, String commitId) {
        return revitFilesMapper.searchFilter(streamId, branchId, commitId);
    }

    @Override
    public boolean addRevitFile(RevitFiles revitFiles) {
        return revitFilesMapper.addRevitFile(revitFiles);
    }

    @Override
    public boolean deleteRevitFileById(String id) {
        return revitFilesMapper.deleteRevitFileById(id);
    }
}
