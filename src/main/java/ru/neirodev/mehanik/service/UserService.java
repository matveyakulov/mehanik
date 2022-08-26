package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.dto.SetFieldDTO;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.UserRatingEntity;

import java.util.Optional;

public interface UserService {

    Optional<UserEntity> getById(Long id);

    UserEntity save(UserDTO userDTO);

    void delete(UserEntity userEntity);

    void setField(SetFieldDTO request);

    void update(UserDTO userDTO, UserEntity userEntity);

    UserEntity getCurrentUser();

    Optional<UserEntity> findByPhone(String phone);

    double getRatingById(Long id);

    void addRatingRow(Long id, Double value);

    Optional<UserRatingEntity> getRatingRowByUserToId(Long userToId);
}
