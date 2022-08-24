package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.entity.UserRating;

import java.util.Optional;

public interface UserService {

    Optional<User> getById(Long id);

    User save(UserDTO userDTO);

    void delete(User user);

    void setField(SetFieldRequest request);

    void update(UserDTO userDTO, User user);

    Optional<User> getByPhone(String phone);

    double getRatingById(Long id);

    void addRatingRow(Long id, Double value);

    Optional<UserRating> getRatingRowByUserToId(Long userToId);
}
