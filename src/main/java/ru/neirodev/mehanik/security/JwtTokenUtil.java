package ru.neirodev.mehanik.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Minu <<a href=minu-moto@mail.ru>minu-moto@mail.ru</a>>
 * @since 03.06.2022 22:45:10
 */
@Component
@PropertySource("file:${catalina.base}/conf/mehanik.properties")
public class JwtTokenUtil {

    /**
     * Время жизни токена в минутах
     */
    public static final int ACCESS_TOKEN_VALIDITY_MINUTES = 30;

    @Value("${jwt.secret}")
    private String secret;

    public Long getUserIdFromToken(String token) {
        return Long.valueOf((Integer)getAllClaimsFromToken(token).get("uid"));
    }

    public static Long getUserIdFromPrincipal() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @SuppressWarnings("unchecked")
    public String getPermsFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("role");
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String generateToken(long userId, String login, String issuer, String role) {
        Claims claims = Jwts.claims();
		claims.setSubject(login);
		claims.setIssuer(issuer);
		claims.setIssuedAt(new Date());
		claims.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MINUTES * 60 * 1000));
        claims.put("uid", userId);
        claims.put("role", role);

        return Jwts.builder()
        		.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}