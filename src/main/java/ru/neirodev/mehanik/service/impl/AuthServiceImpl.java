package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.webjars.NotFoundException;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.entity.security.Session;
import ru.neirodev.mehanik.repository.SessionRepository;
import ru.neirodev.mehanik.repository.UserRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static ru.neirodev.mehanik.security.JwtFilter.COOKIE_NAME;

@Service
public class AuthServiceImpl implements AuthService {

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthServiceImpl(SessionRepository sessionRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    @Override
    public Session login(String login, String password, HttpServletRequest request, HttpServletResponse response) {
        Long uid = userRepository.login(login, password);
        Optional<User> user = null;
        if (uid != null)
            user = userRepository.findById(uid);
        if ((user == null) || user.isEmpty())
            throw new NotFoundException("Пользователь не найден");

        Session session = new Session();
        session.setUserId(uid);
        refreshSession(session, user.get(), request, response);
        return session;
    }

    private void refreshSession(Session session, User user, HttpServletRequest request, HttpServletResponse response) {
        session.setAccessToken(jwtTokenUtil.generateToken(user.getId(), user.getPhone(),
                request.getRequestURL().toString(), user.getRole().getName()));
        session.setRefreshToken(UUID.randomUUID().toString());
        session.setUseragent(request.getHeader(HttpHeaders.USER_AGENT));
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null)
            remoteAddr = request.getRemoteAddr();
        session.setUserIp(remoteAddr);
        session.setLastLogin(new Date());
        sessionRepository.save(session);

        Cookie cookie = new Cookie(COOKIE_NAME, session.getAccessToken());
        cookie.setPath("/");
        cookie.setMaxAge(JwtTokenUtil.ACCESS_TOKEN_VALIDITY_MINUTES * 60);
        cookie.setHttpOnly(true);
        if ("https".equalsIgnoreCase(request.getScheme()))
            cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Transactional
    @Override
    public Session refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        Optional<Session> session = sessionRepository.findSessionByRefreshToken(refreshToken);
        if (session.isEmpty()) {
            return null;
        }
        refreshSession(session.get(), userRepository.findById(session.get().getUserId()).get(), request, response);
        return session.get();
    }

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
        String accessToken = (cookie != null) ? cookie.getValue() : null;
        Optional<Session> session = sessionRepository.findSessionByAccessToken(accessToken);
        if (!session.isEmpty()) {
            session.get().setDel(true);
        }
        cookie = new Cookie(COOKIE_NAME, "");
        cookie.setPath("/");
        // 0 - удаляем куку
        cookie.setMaxAge(0);
        // кука используется только в запросах, через JavaScript получить её нельзя
        cookie.setHttpOnly(true);
        // если соединение защищённое, то устанавливаем куке флаг чтоб не светить её в незащищённых соединениях
        if ("https".equalsIgnoreCase(request.getScheme()))
            cookie.setSecure(true);
        response.addCookie(cookie);
    }

}