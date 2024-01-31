package com.yekj.csinquiry.config;

import com.yekj.csinquiry.user.entity.Group;
import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.security.SecurityUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String salt;

    private final SecurityUserDetailsService securityUserDetailsService;
    private Key secretKey;

    /**
     * jwt 토큰 생성
     * @param user
     * @return
     */
    public final String createJwt(User user) {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));

        // 그룹
        Group group = user.getGroup();
        String strGroupId = group == null ? "" : group.getId().toString();

        // 시간설정
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expMillis = nowMillis + 24 * 60 * 60 * 1000; // 24시간
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .subject(user.getId().toString())
                .expiration(exp)
                .claim("email", user.getEmail())
                .claim("group", strGroupId)
                .signWith(secretKey)
                .compact();
    }

}
