package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.BomFile;

import java.util.List;

public interface BomFileService extends BaseService<BomFile> {

    BomFile getById(String id);

    BomFile getByType(String type);

    List<BomFile> getLatestTemplateFile();

    boolean addBomFile(BomFile bomFile);

    boolean deleteBomFile(String id);

}
