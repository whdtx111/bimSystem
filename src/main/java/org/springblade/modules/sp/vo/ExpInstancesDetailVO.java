package org.springblade.modules.sp.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ExpInstances详细信息VO
 *
 * @author AI
 * @since 2024-05-14
 */
@Data
public class ExpInstancesDetailVO {
    private String objId;
    private String category;
    private String family;
    private String type;
    private String elementId;
    private List<Map<String, String>> parameters;
}