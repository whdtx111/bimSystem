package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS("postgresql")
@Slf4j
public class BranchService {

    @Autowired
    private BranchMapper branchMapper;

    public String getLatestCommitsId(String branchId) {
        return branchMapper.getLatestCommitsIdByBranchId(branchId);
    }

}
