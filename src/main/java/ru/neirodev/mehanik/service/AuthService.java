package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.dto.SmsDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.security.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    Session refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

    boolean sendSms(SmsDTO smsDTO);

    Session startSession(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response);
}
