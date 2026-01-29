package org.springblade.modules.sp.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.dto.LibDicDetailDTO;
import org.springblade.modules.sp.entity.LibDicDetail;
import org.springblade.modules.sp.mapper.LibDicDetailMapper;
import org.springblade.modules.sp.service.LibDicDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class LibDicDetailServiceImpl extends BaseServiceImpl<LibDicDetailMapper, LibDicDetail> implements LibDicDetailService {

    @Autowired
    private LibDicDetailMapper libDicDetailMapper;

    @Override
    public List<LibDicDetail> getLibDicDetailByPid(String pid) {
        try {
            return libDicDetailMapper.getLibDicDetailByPid(pid);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LibDicDetailDTO> listAllWithCode(){
        try {
            return libDicDetailMapper.listAllWithCode();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public  LibDicDetail getLibDicDetailById(String id) {
        try {
            return libDicDetailMapper.getLibDicDetailById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addLibDicDetail(LibDicDetail libDicDetail) {
        try {
            libDicDetailMapper.addLibDicDetail(libDicDetail);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addLibDicDetailList(List<LibDicDetail> libDicDetails){
        try {
            libDicDetailMapper.addLibDicDetailList(libDicDetails);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibDicDetail(LibDicDetail libDicDetail) {
        try {
            libDicDetailMapper.updateLibDicDetail(libDicDetail);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateLibDicDetailStatus(String id,Integer status) {
        try {
            libDicDetailMapper.updateLibDicDetailStatus( id, status);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLibDicDetailById(String id) {
        try {
            libDicDetailMapper.deleteLibDicDetailById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLibDicDetailByPid(String pid) {
        try {
            libDicDetailMapper.deleteLibDicDetailByPid(pid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
