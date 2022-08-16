package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
