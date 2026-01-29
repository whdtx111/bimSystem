package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.Ebs;

@Data
public class PageEbs {

    private Page<Ebs> pageEbs;
    private Long total;

    public PageEbs(Page<Ebs> pageEbs, Long total) {
        this.pageEbs = pageEbs;
        this.total = total;
    }

    public PageEbs() {
    }
}
