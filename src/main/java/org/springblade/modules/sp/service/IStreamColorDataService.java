package org.springblade.modules.sp.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.sp.dto.StreamColorDataDTO;
import org.springblade.modules.sp.entity.StreamColorData;

import java.util.List;

/**
 * Stream颜色数据服务类
 *
 * @author auto-generated
 * @since 2026-01-05
 */
public interface IStreamColorDataService extends BaseService<StreamColorData> {

    /**
     * 保存Stream颜色数据（支持批量保存）
     *
     * @param dto 请求DTO
     * @return 是否保存成功
     */
    boolean saveColorData(StreamColorDataDTO dto);

    /**
     * 根据streamId和commitId查询数据列表
     *
     * @param streamId StreamID
     * @param commitId CommitID
     * @return 数据列表
     */
    List<StreamColorData> getByStreamAndCommit(String streamId, String commitId);

    /**
     * 根据streamId和commitId删除数据
     *
     * @param streamId StreamID
     * @param commitId CommitID
     * @return 是否删除成功
     */
    boolean deleteByStreamAndCommit(String streamId, String commitId);

}
