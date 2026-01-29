package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.SubscriptionConfig;
@Data
public class PageSubVO {

    private Page<SubscriptionConfig> page;
    private Long total;


}
