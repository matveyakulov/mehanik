package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.entity.UserRating;
import ru.neirodev.mehanik.mapper.UserMapper;
import ru.neirodev.mehanik.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRatingRepository userRatingRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRatingRepository = userRatingRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getById(Long id) {
        Optional<User> repUser = userRepository.findById(id);
        repUser.ifPresent(user -> user.setRating(getRatingById(id)));
        return repUser;
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
        user.setRole(roleRepository.getRoleByName("USER"));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    @Override
    public double getRatingById(Long id) {
        Long count = userRatingRepository.countByUserToId(id);
        if(count > 0) {
            return userRatingRepository.sumByUserToId(id) / count;
        }
        return 0;
    }

    @Transactional
    @Override
    public void addRatingRow(Long id, Double value) {
        UserRating userRating = new UserRating();
        userRating.setUserToId(id);
        userRating.setValue(value);
        Long userFromId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRating.setUserFromId(userFromId);
        userRatingRepository.save(userRating);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserRating> getRatingRowByUserToId(Long userToId) {
        Long userFromId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRatingRepository.findUserRatingByUserFromIdAndUserToId(userFromId, userToId);
    }
}
