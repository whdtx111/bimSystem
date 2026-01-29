package org.springblade.modules.sp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smartlink.jwt.rsa")
public class JwtRsaProperties {

    private String privateKey;

    private String publicKey;

    private Long expireSeconds = 259200L; // 72 hours

    private String issuer = "sp-server";

    private String audience;

}
