package org.springblade.modules.sp.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springblade.modules.sp.entity.Stream;
import org.springblade.modules.sp.mapper.StreamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
@Slf4j
public class StreamService {

    @Autowired
    private StreamMapper streamMapper;


    public List<Stream> getAllStreams() {
        return streamMapper.getAllStreams();
    }

    public String getStreamNameById(String id) {
        try {
            return streamMapper.getStreamNameById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
