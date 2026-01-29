package org.springblade.modules.sp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.modules.sp.config.JwtRsaProperties;
import org.springblade.modules.sp.dto.RoleAuthStreamDTO;
import org.springblade.modules.sp.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RsaJwtUtil {

    private static final Logger log = LoggerFactory.getLogger(RsaJwtUtil.class);

    private final JwtRsaProperties properties;

    private PrivateKey cachedPrivateKey;

    private PublicKey cachedPublicKey;

    @Autowired
    public RsaJwtUtil(JwtRsaProperties properties) {
        this.properties = properties;
    }

    public String generateToken(Users user, List<RoleAuthStreamDTO> authRoles) {
        if (user == null || !StringUtils.hasText(user.getId())) {
            throw new IllegalArgumentException("user or userId is empty");
        }
        PrivateKey privateKey;
        try {
            privateKey = getPrivateKey();
        } catch (IllegalStateException ex) {
            log.warn("RSA private key is not configured, skip JWT generation", ex);
            return null;
        }
        Date now = new Date();
        long expireSeconds = properties.getExpireSeconds() != null ? properties.getExpireSeconds() : 3600L;
        Date exp = new Date(now.getTime() + expireSeconds * 1000);
        String jti = UUID.randomUUID().toString();

        // 使用LinkedHashSet保持顺序并去重
        List<String> roles = new ArrayList<>();
        if (authRoles != null) {
            // 先用Set去重，再转为List
            java.util.Set<String> uniqueRoles = new java.util.LinkedHashSet<>();
            for (RoleAuthStreamDTO dto : authRoles) {
                if (dto != null && StringUtils.hasText(dto.getAuthName())) {
                    uniqueRoles.add(dto.getAuthName());
                }
            }
            roles.addAll(uniqueRoles);
        }

        JwtBuilder builder = Jwts.builder()
                .setId(jti)
                .setSubject(user.getId())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .claim("userId", user.getId())
                .claim("roles", roles);

        if (StringUtils.hasText(properties.getIssuer())) {
            builder.setIssuer(properties.getIssuer());
        }
        if (StringUtils.hasText(properties.getAudience())) {
            builder.setAudience(properties.getAudience());
        }

        return builder.signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    public Claims parseToken(String token) {
        try {
            PublicKey publicKey = getPublicKey();
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Failed to parse RSA JWT", e);
            return null;
        }
    }

    private PrivateKey getPrivateKey() {
        if (cachedPrivateKey != null) {
            return cachedPrivateKey;
        }
        String pem = properties.getPrivateKey();
        if (!StringUtils.hasText(pem)) {
            throw new IllegalStateException("jwt.rsa.private-key is not configured");
        }
        cachedPrivateKey = loadPrivateKeyFromPem(pem);
        return cachedPrivateKey;
    }

    private PublicKey getPublicKey() {
        if (cachedPublicKey != null) {
            return cachedPublicKey;
        }
        String pem = properties.getPublicKey();
        if (!StringUtils.hasText(pem)) {
            throw new IllegalStateException("jwt.rsa.public-key is not configured");
        }
        cachedPublicKey = loadPublicKeyFromPem(pem);
        return cachedPublicKey;
    }

    private PrivateKey loadPrivateKeyFromPem(String pem) {
        try {
            String content = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(content);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA private key", e);
        }
    }

    private PublicKey loadPublicKeyFromPem(String pem) {
        try {
            String content = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(content);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA public key", e);
        }
    }
}
