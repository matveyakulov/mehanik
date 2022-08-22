package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.entity.UserRating;
import ru.neirodev.mehanik.mapper.UserMapper;
import ru.neirodev.mehanik.repository.UserRatingRepository;
import ru.neirodev.mehanik.repository.UserRepository;
import ru.neirodev.mehanik.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserRatingRepository userRatingRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRatingRepository userRatingRepository) {
        this.userRepository = userRepository;
        this.userRatingRepository = userRatingRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User save(UserDTO userDTO) {
        User user = new User();
        UserMapper.INSTANCE.updateUserFromDTO(userDTO, user);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void setField(SetFieldRequest request) {
        Optional<User> repUser = userRepository.findById(request.getId());
        if (repUser.isPresent()) {
            User user = repUser.get();
            try {
                Field field = User.class.getDeclaredField(request.getFieldName());
                field.setAccessible(true);
                field.set(user, request.getFieldValue());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Поле не существует", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Поле не изменено из-за ошибки", e);
            }
        } else throw new EntityNotFoundException("Пользователь с таким id не найден");
    }

    @Transactional
    @Override
    public void update(UserDTO userDTO, User user) {
        UserMapper.INSTANCE.updateUserFromDTO(userDTO, user);
    }

    @Transactional(readOnly = true)
    @Override
    public double getRatingById(Long id) {
        return userRatingRepository.sumByUserToId(id) / userRatingRepository.countByUserToId(id);
    }

    @Transactional
    @Override
    public void addRatingRow(Long id, Double value) {
        UserRating userRating = new UserRating();
        userRating.setUserToId(id);
        userRating.setValue(value);
        // TODO после готовой авторизации
//        Long userFromId = (Long) SecurityContextHolder.getContext().getAuthentication();
//        userRating.setUserFromId(userFromId);
        userRatingRepository.save(userRating);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserRating> getRatingRowByUserToId(Long userToId) {
        // TODO после готовой авторизации
//        Long userFromId = (Long) SecurityContextHolder.getContext().getAuthentication();
        return userRatingRepository.findUserRatingByUserFromIdAndUserToId(userFromId, userToId);
    }
}
