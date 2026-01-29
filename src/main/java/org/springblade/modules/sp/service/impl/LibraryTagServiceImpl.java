package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.LibraryTag;
import org.springblade.modules.sp.mapper.LibraryTagMapper;
import org.springblade.modules.sp.service.LibraryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class LibraryTagServiceImpl extends BaseServiceImpl<LibraryTagMapper, LibraryTag> implements LibraryTagService {

    @Autowired
    private LibraryTagMapper libraryTagMapper;


    @Override
    public LibraryTag getById(String id) {
        try {
            LibraryTag libraryTag = libraryTagMapper.getById(id);
            return libraryTag;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public LibraryTag getByName(String tag) {
        try {
            LibraryTag libraryTag = libraryTagMapper.getByName(tag);
            return libraryTag;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<LibraryTag> getAllLibraryTag() {
        try {
            List<LibraryTag> libraryTags = libraryTagMapper.getAllLibraryTag();
            return libraryTags;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addLibraryTag(LibraryTag libraryTag) {
        try {
            if (ObjectUtils.isEmpty(libraryTag)) {
                return false;
            }
            libraryTagMapper.addLibraryTag(libraryTag);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateLibraryTag(LibraryTag libraryTag) {
        try {
            if (ObjectUtils.isEmpty(libraryTag)) {
                return false;
            }
            libraryTagMapper.updateLibraryTag(libraryTag);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteLibraryTag(String id) {
        try {
            libraryTagMapper.deleteLibraryTag(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
