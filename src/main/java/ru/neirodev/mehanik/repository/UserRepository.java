package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.dto.UserRatingDTO;
import ru.neirodev.mehanik.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT SUM(ur.value) as count, COUNT(*) as sum from core.users_rating ur WHERE ur.user_to_id = :userToId", nativeQuery = true)
    UserRatingDTO getValuesByUserToId(Long userToId);
}
