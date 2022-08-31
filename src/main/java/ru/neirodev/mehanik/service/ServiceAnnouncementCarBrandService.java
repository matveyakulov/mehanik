package ru.neirodev.mehanik.service;

import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.entity.ServiceAnnouncementCarBrandEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceAnnouncementCarBrandService {

    Optional<ServiceAnnouncementCarBrandEntity> findById(Long id);

    void save(ServiceAnnouncementCarBrandEntity serviceAnnouncementCarBrandEntity);
    void delete(ServiceAnnouncementCarBrandEntity serviceAnnouncementCarBrandEntity);

    void deleteByAnnouncementId(Long id);

    void save(Long id, List<String> brands);

    @Transactional
    void delete(Long id, List<String> brands);
}
