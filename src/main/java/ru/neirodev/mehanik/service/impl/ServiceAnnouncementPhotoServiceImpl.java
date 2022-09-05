package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.entity.ServiceAnnouncementPhotoEntity;
import ru.neirodev.mehanik.repository.ServiceAnnouncementPhotoRepository;
import ru.neirodev.mehanik.service.ServiceAnnouncementPhotoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceAnnouncementPhotoServiceImpl implements ServiceAnnouncementPhotoService {

    private final ServiceAnnouncementPhotoRepository serviceAnnouncementPhotoRepository;

    @Autowired
    public ServiceAnnouncementPhotoServiceImpl(ServiceAnnouncementPhotoRepository serviceAnnouncementPhotoRepository) {
        this.serviceAnnouncementPhotoRepository = serviceAnnouncementPhotoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ServiceAnnouncementPhotoEntity> findById(Long id) {
        return serviceAnnouncementPhotoRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteByAnnouncementId(Long id) {
        serviceAnnouncementPhotoRepository.deleteAllByServiceAnnouncementId(id);
    }

    @Transactional
    @Override
    public void save(Long id, List<String> photos) {
        List<ServiceAnnouncementPhotoEntity> entities = photos.stream()
                .map(photo -> new ServiceAnnouncementPhotoEntity(id, photo)).collect(Collectors.toList());
        serviceAnnouncementPhotoRepository.saveAll(entities);
    }

    @Transactional
    @Override
    public void delete(Long id, List<String> photos) {
        for (String photo : photos) {
            serviceAnnouncementPhotoRepository.deleteByServiceAnnouncementIdAndPhoto(id, photo);
        }
    }
}
