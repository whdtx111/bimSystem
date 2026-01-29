package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.DwgFile;

import java.util.List;

/**
 * DWG文件服务类
 * @author dengtx
 */

public interface DwgService extends BaseService<DwgFile> {

    /**
     * 拿记录
     * @param id
     * @return
     */
    DwgFile getById(String id);

    /**
     * 根据mapId拿DWGFILE
     * @param mapId
     * @return
     */
    public DwgFile getByMapId(String mapId,String projectId);

    /**
     * 拿到模型下所有dwg文件
     * @param projectId
     * @return
     */
    public List<DwgFile> getAllDwg(String projectId);

    /**
     * 添加dwgfile
     * @param dwgFile
     * @return
     */
    public boolean addDwgFile(DwgFile dwgFile);

    /**
     * 删除dwgfile
     * @param mapId
     * @return
     */
    public boolean deleteDwgFile(String mapId,String projectId);

    /**
     * 更新dwgfile
     * @param dwgFile
     * @return
     */
    public boolean updateDwgFile(DwgFile dwgFile);

    /**
     * 文件关联模型
     * @param id
     * @param objectId
     * @return
     */
    boolean linkObjects(String id,String[] objectId);
}
