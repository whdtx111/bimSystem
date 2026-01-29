package org.springblade.modules.sp.dto;

import lombok.Data;
import org.springblade.modules.sp.entity.Library;

import java.util.List;

@Data
public class LibWbsTempDTO {

    private List<Library> librarys;
    private String wbsCode;
    private String tempId;
}
