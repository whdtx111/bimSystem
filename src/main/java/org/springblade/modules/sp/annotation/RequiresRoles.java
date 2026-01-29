package org.springblade.modules.sp.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRoles {
    String auth();           // 权限名称
    int level() default 1;   // 所需权限等级，默认为1(可查看)
}