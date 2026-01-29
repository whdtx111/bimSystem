package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.springblade.modules.sp.entity.DwgFile;

import java.util.List;

public interface DwgFileMapper extends BaseMapper<DwgFile> {

    DwgFile getById(String id);

    /**
     * 根据mapId拿DWGFILE
     * @param mapId
     * @return
     */
    DwgFile getByMapId(String mapId,String projectId);

    /**
     * 拿到模型下所有dwg文件
     * @param projectId
     * @return
     */
    List<DwgFile> getAllDwg(String projectId);
    /**
     * 添加dwgfile
     * @param dwgFile
     * @return
     */
    boolean addDwgFile(DwgFile dwgFile);
    /**
     * 删除dwgfile
     * @param mapId
     * @return
     */
    boolean deleteDwgFile(String mapId,String projectId);
    /**
     * 更新dwgfile
     * @param dwgFile
     * @return
     */
    boolean updateDwgFile(DwgFile dwgFile);

    /**
     * 文件关联模型
     * @param id
     * @param objectId
     * @return
     */
    boolean linkObjects(String id,String[] objectId);
}
