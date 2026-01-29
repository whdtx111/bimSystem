package org.springblade.modules.sp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.WebUtil;
import org.springblade.modules.sp.constant.SpConstant;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class SignAuth {

    public static boolean preHandle() {
        HttpServletRequest request = WebUtil.getRequest();
        // 打印request请求头
        String appId = request.getHeader("appId");
        if (StringUtils.isBlank(appId)) {
            log.debug("appId不能为空...........");
            return false;
        }
        String timestampStr = request.getHeader("timestamp");
        if (StringUtils.isBlank(timestampStr)) {
            log.debug("timestamp不能为空...........");
            return false;
        }
        String sign = request.getHeader("sign");
        if (StringUtils.isBlank(sign)) {
            log.debug("sign不能为空...........");
            return false;
        }
        String nonce = request.getHeader("nonce");
        if (StringUtils.isBlank(nonce)) {
            log.debug("nonce不能为空...........");
            return false;
        }
        // 后端加盐
        String  join = appId + SpConstant.APP_SECRET + timestampStr + nonce;
        String signEcrypt = MD5Util.md5(join);
        long timestamp = 0;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //1.前端传过来的时间戳与服务器当前时间戳差值大于180，则当前请求的timestamp无效
        Long timeMillis = System.currentTimeMillis();
        Long abs = Math.abs((timestamp - timeMillis) / 1000);
        if (Math.abs((timestamp - timeMillis) / 1000) > 180) {
            log.debug("timestamp无效...........");
            return false;
        }
        //3.通过后台MD5重新签名校验与前端签名sign值比对，确认当前请求数据是否被篡改
        if (!(sign.equalsIgnoreCase(signEcrypt))) {
            log.debug("sign签名校验失败...........");
            return false;
        }
        log.debug("签名校验通过，放行...........");
        //5.放行
        return true;
    }

}
