package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.entity.security.Session;

public interface AuthService {

    Session login(String login, String password) throws Exception;

    Session refreshToken(String refreshToken) throws Exception;

    void logout() throws Exception;

}
