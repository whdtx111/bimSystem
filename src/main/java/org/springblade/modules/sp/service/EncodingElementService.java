package org.springblade.modules.sp.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.modules.sp.entity.EncodingElement;

import java.util.List;

public interface EncodingElementService {

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
