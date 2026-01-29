package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.entity.StreamList;

import java.util.List;

public interface StreamListService extends BaseService<StreamList> {

    List<StreamList> getStreamList();

    StreamList getStreamListById(String id);

    boolean addStreamList(StreamList streamList);

    boolean updateStreamList(StreamList streamList);

    boolean deleteStreamList(String id);
}
