package org.springblade.modules.sp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.EncodingElement;


import java.util.List;

@Mapper
public interface EncodingElementMapper extends BaseMapper<EncodingElement> {

        EncodingElement getById(String id);

        EncodingElement getEncodingElement(String streamId, String branchId, String elementId, String encodingId);

        List<EncodingElement> getEncodingElements(String streamId, String branchId, String elementId, String[] encodingId);

        List<EncodingElement> getEncodingElementsNoEncodingId(String streamId, String branchId,String elementId);

        List<EncodingElement> getAllEncodingElements();

        List<EncodingElement> filterEncodingElement(@Param("streamId") String streamId, @Param("branchId") String branchId, @Param("elementId") String elementId,@Param("encodingId") String encodingId);

        boolean addEncodingElement(EncodingElement encodingElement);

        boolean updateEncodingElement(EncodingElement encodingElement);

        boolean deleteEncodingElement(String id);

}
