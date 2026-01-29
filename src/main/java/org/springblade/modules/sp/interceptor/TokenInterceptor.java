package org.springblade.modules.sp.interceptor;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.modules.sp.utils.RsaJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token验证拦截器
 * 拦截所有请求，验证请求头中是否包含有效的token
 * 检查token是否存在于Redis中，是否合法
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RsaJwtUtil rsaJwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token（格式：4d8ec92330f1ad90a356781cc5846c7a75b60c1c8d等随机字符串）
        String authorization = request.getHeader("Authorization");

        // 如果token为空或null，拒绝请求
        if (authorization == null || authorization.trim().isEmpty()) {
            handleUnauthorized(response, "缺少token，请求被拒绝");
            return false;
        }

        String token = authorization.trim();

        if (token.length() >= 7 && token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String jwt = token.substring(7).trim();
            if (jwt.isEmpty()) {
                handleUnauthorized(response, "token格式不正确");
                return false;
            }
            Claims claims = rsaJwtUtil.parseToken(jwt);
            if (claims == null) {
                handleUnauthorized(response, "token不合法或已过期，请重新登录");
                return false;
            }
            return true;
        }

        // 从Redis中获取token信息
        // Redis存储结构：key=token字符串, value=包含userId、authRole、user等字段的JSON字符串或JSONObject
        Object tokenObj = redisUtil.get(token);

        // token不存在于Redis中，说明token不合法或已过期
        if (tokenObj == null) {
            handleUnauthorized(response, "token不合法或已过期，请重新登录");
            return false;
        }

        // 验证token内容是否合法
        try {
            // 将Object安全地转换为JSONObject，兼容 String / JSONObject 等形式
            JSONObject tokenJson;
            if (tokenObj instanceof JSONObject) {
                tokenJson = (JSONObject) tokenObj;
            } else {
                tokenJson = JSONObject.parseObject(tokenObj.toString());
            }

            if (tokenJson == null) {
                handleUnauthorized(response, "token解析失败");
                return false;
            }

            // 检查必要字段是否存在
            if (!tokenJson.containsKey("userId") || !tokenJson.containsKey("token")) {
                handleUnauthorized(response, "token格式不正确");
                return false;
            }

            // token验证通过，允许请求继续
            return true;

        } catch (Exception e) {
            handleUnauthorized(response, "token解析失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 处理未授权的请求
     */
    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"success\":false,\"msg\":\"" + message + "\"}");
    }
}
