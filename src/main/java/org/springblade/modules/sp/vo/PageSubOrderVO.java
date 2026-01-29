package org.springblade.modules.sp.vo;

import com.github.pagehelper.Page;
import lombok.Data;
import org.springblade.modules.sp.entity.SubscriptionOrder;
@Data
public class PageSubOrderVO {

    private Page<SubscriptionOrderDetailVO> page;
    private Long total;
}
