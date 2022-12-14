package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;
import ru.neirodev.mehanik.dto.SmsDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.security.Session;
import ru.neirodev.mehanik.repository.SessionRepository;
import ru.neirodev.mehanik.repository.UserRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.AuthService;
import ru.neirodev.mehanik.sms.SmsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import static ru.neirodev.mehanik.security.JwtFilter.COOKIE_NAME;

@Service
public class AuthServiceImpl implements AuthService {

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final SmsService smsService;

    @Autowired
    public AuthServiceImpl(SessionRepository sessionRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, SmsService smsService) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.smsService = smsService;
    }

    @Transactional
    @Override
    public Session startSession(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response) {
        Session session = new Session();
        session.setUserId(userEntity.getId());
        refreshSession(session, userEntity, request, response);
        return session;
    }

    private void refreshSession(Session session, UserEntity userEntity, HttpServletRequest request, HttpServletResponse response) {
        session.setAccessToken(jwtTokenUtil.generateToken(userEntity.getId(), userEntity.getPhone(),
                request.getRequestURL().toString(), userEntity.getRole().getName()));
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
        // 0 - ?????????????? ????????
        cookie.setMaxAge(0);
        // ???????? ???????????????????????? ???????????? ?? ????????????????, ?????????? JavaScript ???????????????? ???? ????????????
        cookie.setHttpOnly(true);
        // ???????? ???????????????????? ????????????????????, ???? ?????????????????????????? ???????? ???????? ???????? ???? ?????????????? ???? ?? ???????????????????????? ??????????????????????
        if ("https".equalsIgnoreCase(request.getScheme()))
            cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Transactional
    @Override
    public boolean sendSms(SmsDTO smsDTO) {

//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String params = String.format("phone=%s&secret=%s&message=%s", smsDTO.getPhone(), smsDTO.getSecret(),
//                "?????? ??????????????????????????: " + smsDTO.getCode());
//        HttpEntity<?> httpEntity = new HttpEntity<>(params, httpHeaders);
//        restTemplate.postForObject("https://carwash.neirodev.ru/sms", httpEntity, String.class);
        try (Scanner scanner = new Scanner(
                new URL("https://carwash.neirodev.ru/sms" + "?secret="
                        + URLEncoder.encode(smsDTO.getSecret(), StandardCharsets.UTF_8) + "&phone="
                        + URLEncoder.encode(smsDTO.getPhone(), StandardCharsets.UTF_8) + "&message="
                        + URLEncoder.encode("?????? ??????????????????????????: " + smsDTO.getCode(), StandardCharsets.UTF_8)).openStream(),
                StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A").hasNext();
        } catch (Exception e) {
            return false;
        }
        Optional<UserEntity> repUser = userRepository.findByPhone(smsDTO.getPhone());
        UserEntity userEntity;
        if(repUser.isPresent()){
            userEntity = repUser.get();
            userEntity.setSmscode(smsDTO.getCode());
        } else {
            userEntity = new UserEntity();
            userEntity.setPhone(smsDTO.getPhone());
            userEntity.setSmscode(smsDTO.getCode());
        }
        userRepository.save(userEntity);
        return true;
    }
}