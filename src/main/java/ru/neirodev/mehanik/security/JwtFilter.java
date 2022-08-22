package ru.neirodev.mehanik.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(asyncSupported = true)
@Component
public class JwtFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "access_token";
    public static final String EXPECTED_AUTH_SCHEME = "bearer";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
    	String path = request.getServletPath() + ((request.getPathInfo() != null) ? request.getPathInfo() : "");
        return "/".equals(path) || path.startsWith("/swagger-ui") || path.startsWith("/v1/openapi");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String encodedJwtToken = getHeaderJwtToken(request);
        if (encodedJwtToken == null)
            encodedJwtToken = getCookieJwtToken(request);

        try {
            if ((encodedJwtToken != null) && !encodedJwtToken.isEmpty()) {
                String role = jwtTokenUtil.getPermsFromToken(encodedJwtToken);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(true,
                        jwtTokenUtil.getUserIdFromToken(encodedJwtToken), Set.of(authority)));
            } else
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(true, null, new HashSet<>()));
        } catch (ExpiredJwtException ex) {
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(false, null, new HashSet<>()));
        }
        filterChain.doFilter(request, response);
    }

    private static String getCookieJwtToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
        return (cookie != null) ? cookie.getValue() : null;
    }

    private static String getHeaderJwtToken(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] parts = (auth == null) ? null : auth.split(" ");
        if ((parts == null) || !EXPECTED_AUTH_SCHEME.equalsIgnoreCase(parts[0]) || (parts.length != 2))
            return null;
        return parts[1];
    }

}