package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.LibraryTag;

import java.util.List;

public interface LibraryTagService extends BaseService<LibraryTag> {

    LibraryTag getById(String id);

    LibraryTag getByName(String tag);

    List<LibraryTag> getAllLibraryTag();

    boolean addLibraryTag(LibraryTag libraryTag);

    boolean updateLibraryTag(LibraryTag libraryTag);

    boolean deleteLibraryTag(String id);
}
