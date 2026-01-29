package org.springblade.modules.sp.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.sp.entity.EncodingElement;
import org.springblade.modules.sp.mapper.EncodingElementMapper;
import org.springblade.modules.sp.service.EncodingElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS("postgresql")
public class EncodingElementServiceImpl extends BaseServiceImpl<EncodingElementMapper, EncodingElement> implements EncodingElementService {

    @Autowired
    private EncodingElementMapper encodingElementMapper;

    @Override
    public EncodingElement getById(String id) {
        try {
            return encodingElementMapper.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EncodingElement getEncodingElement(String streamId, String branchId, String elementId, String encodingId) {
        try {
            return encodingElementMapper.getEncodingElement(streamId, branchId, elementId, encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingElement> getEncodingElements(String streamId, String branchId, String elementId, String[] encodingId) {
        try {
            return encodingElementMapper.getEncodingElements(streamId, branchId, elementId, encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingElement> getAllEncodingElements() {
        try {
            return encodingElementMapper.getAllEncodingElements();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingElement> getEncodingElementsNoEncodingId(String streamId, String branchId, String elementId) {
        try {
            return encodingElementMapper.getEncodingElementsNoEncodingId(streamId, branchId, elementId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EncodingElement> filterEncodingElement(String streamId, String branchId, String elementId,String encodingId) {
        try {
            return encodingElementMapper.filterEncodingElement(streamId, branchId, elementId,encodingId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addEncodingElement(EncodingElement encodingElement) {
        try {
            return encodingElementMapper.addEncodingElement(encodingElement);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEncodingElement(EncodingElement encodingElement) {
        try {
            return encodingElementMapper.updateEncodingElement(encodingElement);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEncodingElement(String id) {
        try {
            return encodingElementMapper.deleteEncodingElement(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
