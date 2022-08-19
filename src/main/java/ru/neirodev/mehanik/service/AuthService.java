package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.entity.security.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    Session login(String login, String password, HttpServletRequest request, HttpServletResponse response);

    Session refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
