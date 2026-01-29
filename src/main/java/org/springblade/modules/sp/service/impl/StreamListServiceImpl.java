package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.StreamList;
import org.springblade.modules.sp.mapper.StreamListMapper;
import org.springblade.modules.sp.service.StreamListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class StreamListServiceImpl extends BaseServiceImpl<StreamListMapper, StreamList> implements StreamListService {

    @Autowired
    private StreamListMapper streamListMapper;

    @Override
    public StreamList getStreamListById(String id) {
      try {
          return streamListMapper.getStreamListById(id);
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
    }

    @Override
    public List<StreamList> getStreamList() {
        try {
            return streamListMapper.getStreamList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addStreamList(StreamList streamList) {
        try {
            return streamListMapper.addStreamList(streamList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStreamList(StreamList streamList) {
        try {
            return streamListMapper.updateStreamList(streamList);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStreamList(String id) {
        try {
            return streamListMapper.deleteStreamListById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
