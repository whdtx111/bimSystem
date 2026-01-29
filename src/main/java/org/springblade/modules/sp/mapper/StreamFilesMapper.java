package org.springblade.modules.sp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.StreamFiles;

import java.util.ArrayList;
import java.util.List;
@Mapper
public interface StreamFilesMapper extends BaseMapper<StreamFiles> {

    /**
     * 根据id查询文件
     * @param id
     * @return
     */
    StreamFiles getById(String id);

    /**
     * 根据入参查询文件
     * @param streamId
     * @param name
     * @return
     */
    StreamFiles getFile(String streamId,String name);

    /**
     * 拿到项目下所有文件
     * @param streamId
     * @return
     */
    List<StreamFiles> getAllStreamFiles(String streamId);

    /**
     * 按类型拿到项目下文件
     * @param streamId
     * @return
     */
    List<StreamFiles> getStreamFiles(String streamId,String type);

    StreamFiles getStreamFileByStreamId(String streamId,String name);

    /**
     * 新增文件
     * @param streamFiles
     * @return
     */
    boolean addStreamFiles(StreamFiles streamFiles);

    /**
     * 删除文件
     * @param id
     * @return
     */
    boolean deleteStreamFiles(String id);

    /**
     * 文件关联模型
     * @param id
     * @param objectId
     * @return
     */
    boolean linkObjects(String id, String[] objectId);

//    /**
//     * 解绑
//     * @param id
//     * @return
//     */
//    boolean unlinkObjects(String id,Object[] objectId);

    /**
     * 文件重命名
     * @param id
     * @param name
     * @return
     */
    boolean rename(String id,String name);


}
