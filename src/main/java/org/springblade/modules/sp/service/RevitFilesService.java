package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.RevitFiles;

import java.util.List;

public interface RevitFilesService extends BaseService<RevitFiles> {

    RevitFiles getById(String id);

    RevitFiles getByName(String streamId, String branchId, String commitId,String name);

    List<RevitFiles> searchFilter(String streamId, String branchId, String commitId);

    boolean addRevitFile(RevitFiles revitFiles);

    boolean deleteRevitFileById(String id);
}
