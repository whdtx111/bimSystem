package org.springblade.modules.sp.service;

import com.github.pagehelper.Page;
import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.Encoding;

import java.util.List;

public interface EncodingService extends BaseService<Encoding> {

    Encoding getById(String id);

    List<Encoding> getAllEncoding();

    Page<Encoding> filterEncoding(String name, String version, String auth, String user, String source,String isfinished, Integer pageSize, Integer currentPage);

    boolean addEncoding(Encoding encoding);

    boolean deleteEncoding(String id);

    boolean updateEncoding(Encoding encoding);

}
