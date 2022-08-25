package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.UserRatingEntity;

import java.util.Optional;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRatingEntity, Long> {

    Long countByUserToId(Long id);

    @Query("SELECT SUM(ur.value) FROM UserRatingEntity ur")
    Double sumByUserToId(Long id);

    Optional<UserRatingEntity> findUserRatingByUserFromIdAndUserToId(Long userFromId, Long userToId);
}
