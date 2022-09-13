package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.UserRatingEntity;
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
    public Optional<UserEntity> findById(Long id) {
        Optional<UserEntity> repUser = userRepository.findById(id);
        repUser.ifPresent(user -> user.setRating(getRatingById(id)));
        return repUser;
    }

    @Transactional
    @Override
    public UserEntity save(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        UserMapper.INSTANCE.updateUserFromDTO(userDTO, userEntity);
        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    @Transactional
    @Override
    public void setField(SetFieldRequest request) {
        Optional<UserEntity> repUser = userRepository.findById(request.getId());
        if (repUser.isPresent()) {
            UserEntity userEntity = repUser.get();
            try {
                Field field = UserEntity.class.getDeclaredField(request.getFieldName());
                field.setAccessible(true);
                field.set(userEntity, request.getFieldValue());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Поле не существует", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Поле не изменено из-за ошибки", e);
            }
        } else throw new EntityNotFoundException("Пользователь с таким id не найден");
    }

    @Transactional
    @Override
    public void update(UserDTO userDTO, UserEntity userEntity) {
        UserMapper.INSTANCE.updateUserFromDTO(userDTO, userEntity);
        userEntity.setRole(roleRepository.getRoleByName("USER"));
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getCurrentUser() {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    @Override
    public double getRatingById(Long id) {
        return userRepository.getRatingById(id);
    }

    @Transactional
    @Override
    public void addRatingRow(Long id, Double value) {
        UserRatingEntity userRatingEntity = new UserRatingEntity();
        userRatingEntity.setUserToId(id);
        userRatingEntity.setValue(value);
        userRatingRepository.save(userRatingEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserRatingEntity> getRatingRowByUserToId(Long userToId) {
        Long userFromId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRatingRepository.findUserRatingByUserFromIdAndUserToId(userFromId, userToId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
