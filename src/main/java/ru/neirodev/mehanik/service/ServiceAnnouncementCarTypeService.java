package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.entity.ServiceAnnouncementCarTypeEntity;
import ru.neirodev.mehanik.enums.CarType;

import java.util.List;
import java.util.Optional;

public interface ServiceAnnouncementCarTypeService {

    Optional<ServiceAnnouncementCarTypeEntity> findById(Long id);

    void save(ServiceAnnouncementCarTypeEntity serviceAnnouncementCarTypeEntity);
    void delete(ServiceAnnouncementCarTypeEntity serviceAnnouncementCarTypeEntity);

    void deleteByAnnouncementId(Long id);

    void delete(Long id, List<CarType> types);

    void save(Long id, List<CarType> types);
}
