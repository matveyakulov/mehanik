package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.UserRating;

import java.util.Optional;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    Long countByUserToId(Long id);

    @Query("SELECT SUM(ur.value) FROM UserRating ur")
    Double sumByUserToId(Long id);

    Optional<UserRating> findUserRatingByUserFromIdAndUserToId(Long userFromId, Long userToId);
}
