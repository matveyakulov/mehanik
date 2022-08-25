package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.UserRatingEntity;

import java.util.Optional;

public interface UserService {

    Optional<UserEntity> getById(Long id);

    UserEntity save(UserDTO userDTO);

    void delete(UserEntity userEntity);

    void setField(SetFieldRequest request);

    void update(UserDTO userDTO, UserEntity userEntity);

    Optional<UserEntity> getByPhone(String phone);

    double getRatingById(Long id);

    void addRatingRow(Long id, Double value);

    Optional<UserRatingEntity> getRatingRowByUserToId(Long userToId);
}
