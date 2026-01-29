package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.Wbs;

@Data
public class PageWbs {

    private Page<Wbs> pageWbs;
    private Long total;

    public PageWbs(Page<Wbs> pageWbs, Long total) {
        this.pageWbs = pageWbs;
        this.total = total;
    }

    public PageWbs() {
    }
}
