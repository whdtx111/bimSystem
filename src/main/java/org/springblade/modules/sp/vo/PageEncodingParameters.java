package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.EncodingParameters;

@Data
public class PageEncodingParameters {

    private Page<EncodingParameters> page;

    private Long total;

    public PageEncodingParameters(Page<EncodingParameters> page, Long total) {
        this.page = page;
        this.total = total;
    }

    public PageEncodingParameters() {
    }
}
