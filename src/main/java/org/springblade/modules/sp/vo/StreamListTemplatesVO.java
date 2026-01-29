package org.springblade.modules.sp.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springblade.modules.sp.entity.NewTemplate;

import java.util.Date;
import java.util.List;

@Data
public class StreamListTemplatesVO {
    private String streamListId;
    private String[] streamIds;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date streamListModifyTime;
    
    private List<NewTemplate> newTemplateList;
}
