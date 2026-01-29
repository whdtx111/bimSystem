package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.EncodingLibrary;


@Data
public class PageEncodingLib {
    private Page<EncodingLibrary> page;
    private Long total;
}
