package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.entity.ServiceAnnouncementPhotoEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceAnnouncementPhotoService {

    Optional<ServiceAnnouncementPhotoEntity> findById(Long id);

    void deleteByAnnouncementId(Long id);

    void save(Long id, List<String> photos);

    void delete(Long id, List<String> photos);
}
