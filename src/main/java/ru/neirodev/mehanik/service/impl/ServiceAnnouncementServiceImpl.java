package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.ServiceType;
import ru.neirodev.mehanik.repository.ServiceAnnouncementRepository;
import ru.neirodev.mehanik.service.ServiceAnnouncementService;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceAnnouncementServiceImpl implements ServiceAnnouncementService {

    private final ServiceAnnouncementRepository serviceAnnouncementRepository;

    @Autowired
    public ServiceAnnouncementServiceImpl(ServiceAnnouncementRepository serviceAnnouncementRepository) {
        this.serviceAnnouncementRepository = serviceAnnouncementRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceAnnouncementDTO> getAllDTOByServiceType(ServiceType serviceType, Pageable pageable) {
        return serviceAnnouncementRepository.findAllByService(serviceType, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceAnnouncementDTO> getAllDTOByServiceType(ServiceType serviceType) {
        return serviceAnnouncementRepository.findAllByService(serviceType);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ServiceAnnouncementEntity> findById(Long id) {
        return serviceAnnouncementRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return serviceAnnouncementRepository.existsById(id);
    }

    @Transactional
    @Override
    public ServiceAnnouncementEntity save(ServiceAnnouncementEntity serviceAnnouncementEntity) {
        return serviceAnnouncementRepository.save(serviceAnnouncementEntity);
    }

    @Transactional
    @Override
    public void delete(ServiceAnnouncementEntity serviceAnnouncementEntity) {
        serviceAnnouncementRepository.delete(serviceAnnouncementEntity);
    }
}
