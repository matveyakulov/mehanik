package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.entity.CarEntity;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<CarEntity> getAll();
    List<CarEntity> getAll(Pageable pageable);

    Optional<CarEntity> findById(Long id);

    CarEntity save(CarEntity car);
    void delete(CarEntity    car);
}
