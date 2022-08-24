package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.security.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findSessionByRefreshToken(String refreshToken);
    Optional<Session> findSessionByAccessToken(String accessToken);

    Optional<Session> findSessionByUserId(Long id);
}
