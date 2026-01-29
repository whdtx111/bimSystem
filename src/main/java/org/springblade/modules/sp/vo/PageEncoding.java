package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.Encoding;

@Data
public class PageEncoding {

    private Page<Encoding> page;
    private Long total;

    public PageEncoding(Page<Encoding> page, Long total) {
        this.page = page;
        this.total = total;
    }

    public PageEncoding() {

    }
}
