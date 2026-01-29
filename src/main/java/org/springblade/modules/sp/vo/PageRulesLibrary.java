package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.RulesLibrary;

@Data
public class PageRulesLibrary {

    private Page<RulesLibrary> pageRulesLibrary;
    private Long total;

    public PageRulesLibrary(Page<RulesLibrary> pageRulesLibrary, Long total) {
        this.pageRulesLibrary = pageRulesLibrary;
        this.total = total;
    }

    public PageRulesLibrary() {
    }
}
