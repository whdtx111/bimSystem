package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.LibDictionary;

import java.util.List;

public interface LibDictionaryService extends BaseService<LibDictionary> {

    List<LibDictionary> getLibDictionaryList();

    LibDictionary getLibDictionaryById(String id);

    boolean addLibDictionary(LibDictionary libDictionary);

    boolean updateLibDictionary(LibDictionary libDictionary);

    boolean updateLibDictionaryStatus(String id,Integer status);

    boolean deleteLibDictionary(String id);
}
