package org.springblade.modules.sp.vo;

import lombok.Data;
import org.springblade.modules.sp.entity.RulesDetail;
import org.springblade.modules.sp.entity.RulesParameters;

import java.util.List;

@Data
public class RulesDetailVO extends RulesDetail {
    private List<RulesParameters> parameters;
}