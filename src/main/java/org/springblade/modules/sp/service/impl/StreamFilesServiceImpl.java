package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.StreamFiles;
import org.springblade.modules.sp.mapper.StreamFilesMapper;
import org.springblade.modules.sp.service.StreamFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
@DS("postgresql")
public class StreamFilesServiceImpl extends BaseServiceImpl<StreamFilesMapper, StreamFiles> implements StreamFilesService {

    @Autowired
    private StreamFilesMapper streamFilesMapper;

    @Override
    public StreamFiles getById(String id){
        try {
            StreamFiles file = streamFilesMapper.getById(id);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public StreamFiles getFile(String streamId,String name){
        try {
            StreamFiles file = streamFilesMapper.getFile(streamId,name);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StreamFiles> getStreamFiles(String streamId){
        try {
            List<StreamFiles> files = streamFilesMapper.getAllStreamFiles(streamId);
            return files;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StreamFiles> getStreamFiles(String streamId,String fileType){
        try {
//            String type = "";
//            if ("drawing".equals(fileType)){
//                    type = "dwg,pdf";
//            }
//            if ("file".equals(fileType)){
//                type="doc,docx,xls,xlsx,ppt";
//            }
//            if ("model".equals(fileType)){
//                type="rvt,ifc,obj,stl";
//            }
            List<StreamFiles> files = streamFilesMapper.getStreamFiles(streamId,fileType);
            return files;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public StreamFiles getStreamFileByStreamId(String streamId,String name){
        try {
            StreamFiles file = streamFilesMapper.getStreamFileByStreamId(streamId,name);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addStreamFiles(StreamFiles streamFiles){
        try {
            if (ObjectUtils.isEmpty(streamFiles)){
                return false;
            }
            return streamFilesMapper.addStreamFiles(streamFiles);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStreamFiles(String id){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            return streamFilesMapper.deleteStreamFiles(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件关联模型
     * @param id
     * @param objectId
     * @return
     */
    @Override
    public boolean linkObjects(String id,String[] objectId){
        try {
            if (StringUtils.isEmpty(id)){
                return false;
            }
            if ( Arrays.toString((objectId)).equals("[[]]")){
                objectId = new String[0];
            }
            return streamFilesMapper.linkObjects(id,objectId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    /**
//     * 解绑
//     * @param id
//     * @return
//     */
//    @Override
//    public boolean unlinkObjects(String id,List<String> objectId){
//        try{
//            if (StringUtils.isEmpty(id)){
//                return false;
//            }
//            return streamFilesMapper
//        }
//    }

    /**
     * 文件重命名
     * @param id
     * @param name
     * @return
     */
    @Override
    public boolean rename(String id,String name){
        try {
            StreamFiles file = streamFilesMapper.getById(id);
            if (ObjectUtils.isEmpty(file)){
                return false;
            }
            boolean res = streamFilesMapper.rename(id, name);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
