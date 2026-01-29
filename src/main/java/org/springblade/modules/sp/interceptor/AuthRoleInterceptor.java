//package org.springblade.modules.sp.interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springblade.core.tool.utils.RedisUtil;
//import org.springblade.modules.sp.annotation.RequiresRoles;
//import org.springblade.modules.sp.constant.AuthPathMapping;
//import org.springblade.modules.sp.entity.RoleAuth;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//@Component
//public class AuthRoleInterceptor implements HandlerInterceptor {
//
////    @Autowired
////    private RedisUtil redisUtil;
////
////    @Override
////    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////        String requestPath = request.getRequestURI();
////
////        // 获取路径对应的权限名称
////        String pathAuth = AuthPathMapping.getAuthByPath(requestPath);
////
////        // 如果是需要自动验证的路径
////        if (pathAuth != null) {
////            return validateAuth(request, response, pathAuth, 1); // 默认需要查看权限(level 1)
////        }
////
////        // 处理自定义注解的权限验证
////        if (handler instanceof HandlerMethod) {
////            HandlerMethod handlerMethod = (HandlerMethod) handler;
////            RequiresRoles requiresRoles = handlerMethod.getMethodAnnotation(RequiresRoles.class);
////
////            if (requiresRoles != null) {
////                return validateAuth(request, response, requiresRoles.auth(), requiresRoles.level());
////            }
////        }
////
////        return true;
////    }
////
////    // 抽取验证逻辑为独立方法
////    private boolean validateAuth(HttpServletRequest request, HttpServletResponse response,
////                                 String authName, int requiredLevel) {
////        String token = request.getHeader("Authorization");
////        if (token == null) {
////            response.setStatus(401);
////            return false;
////        }
////
////        Object tokenObj = redisUtil.get(token);
////        if (tokenObj == null) {
////            response.setStatus(401);
////            return false;
////        }
////
////        JSONObject tokenJson = (JSONObject) tokenObj;
////        List<RoleAuth> authRoles = tokenJson.getObject("authRole", List.class);
////
////        if (authRoles == null || authRoles.isEmpty()) {
////            response.setStatus(403);
////            return false;
////        }
////
////        // 验证权限
////        for (RoleAuth auth : authRoles) {
////            if (auth.getAuthName().equals(authName)) {
////                int currentLevel = Integer.parseInt(auth.getAuthLevel());
////                if (currentLevel >= requiredLevel) {
////                    return true;
////                }
////                response.setStatus(403);
////                return false;
////            }
////        }
////
////        response.setStatus(403);
////        return false;
////    }
//}
