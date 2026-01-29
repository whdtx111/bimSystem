package org.springblade.modules.sp.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huang can/dengtx
 * @since 2024/3/19 11:04
 */

@Data
public class TemplateListDTO{

    private String pid;
    private String name;
    private String wbsGroup;
    private String type;
    private String branchId;
    private String streamId;
    private String detail;
    private Float ranknum;
    private String modifyUser;

//    private List<TemplateListDTO> childrenDTO;
}
