package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import org.webjars.NotFoundException;
import ru.neirodev.mehanik.entity.security.Session;
import ru.neirodev.mehanik.repository.SessionRepository;
import ru.neirodev.mehanik.repository.UserRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.AuthService;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Session login(String login, String password) {
        Long uid = staffRepository.login(login, password);
        Optional<Staff> usr = null;
        if (uid != null)
            usr = staffRepository.findById(uid);
        if ((usr == null) || usr.isEmpty())
            throw new NotFoundException("Пользователь не найден");
        if (!usr.get().getStatus().equals(Status.ACTIVE))
            throw new BadRequestException("Доступ запрещен");

        Session session = new Session();
        session.setTuserId(uid);
        refreshSession(session, usr.get());
        return session;
    }

    private void refreshSession(Session session, Staff usr) {
        session.setAccessToken(jwtTokenUtil.generateToken(usr.getPerson().getId(), usr.getLogin(), request.getRequestURL().toString(),
                ((usr.getRole() != null) && (usr.getRole().getPermissions() != null))
                        ? usr.getRole().getPermissions().stream().map(Permission::getPerm).collect(Collectors.toSet())
                        : null));
        session.setRefreshToken(UUID.randomUUID().toString());
        session.setUseragent(request.getHeader(HttpHeaders.USER_AGENT));
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null)
            remoteAddr = request.getRemoteAddr();
        session.setUserip(remoteAddr);
        session.setLastLogin(new Date());
        sessionRepository.save(session);

        Cookie cookie = new Cookie(COOKIE_NAME, session.getAccessToken());
        cookie.setPath("/");
        // кука живет ACCESS_TOKEN_VALIDITY минут
        cookie.setMaxAge(JwtTokenUtil.ACCESS_TOKEN_VALIDITY * 60);
        // кука используется только в запросах, через JavaScript получить её нельзя
        cookie.setHttpOnly(true);
        // если соединение защищённое, то устанавливаем куке флаг чтоб не светить её в
        // незащищённых соединениях
        if ("https".equalsIgnoreCase(request.getScheme()))
            cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Transactional
    @Override
    public Session refreshToken(String refreshToken) throws Exception {
        Optional<Session> session = sessionRepository.findSessionByRefreshToken(refreshToken);
        if (session.isEmpty())
            throw new NotFoundException("Сессия с данным refresh token'ом не найдена");

        refreshSession(session.get(), staffRepository.findById(session.get().getTuserId()).get());
        return session.get();
    }

    @Transactional
    @Override
    public void logout() throws Exception {
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