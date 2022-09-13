package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.entity.CarEntity;
import ru.neirodev.mehanik.repository.CarRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.CarService;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarEntity> getAll() {
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        return carRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CarEntity> getAll(Pageable pageable) {
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        return carRepository.findAllByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CarEntity> findById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    @Override
    public CarEntity save(CarEntity car) {
        return carRepository.save(car);
    }

    @Transactional
    @Override
    public void delete(CarEntity car) {
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        if(userId.equals(car.getUserId())) {
            carRepository.delete(car);
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return carRepository.existsById(id);
    }
}
