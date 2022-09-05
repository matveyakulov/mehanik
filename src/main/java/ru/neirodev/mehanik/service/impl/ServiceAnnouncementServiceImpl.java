package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.dto.ServiceAnnouncementShowDTO;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.ServiceType;
import ru.neirodev.mehanik.mapper.ServiceAnnouncementMapper;
import ru.neirodev.mehanik.repository.ServiceAnnouncementRepository;
import ru.neirodev.mehanik.service.ServiceAnnouncementService;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
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
    public List<ServiceAnnouncementShowDTO> getAllDTOByServiceType(ServiceType serviceType, Pageable pageable) {
        return serviceAnnouncementRepository.findAllByService(serviceType, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceAnnouncementShowDTO> getAllDTOByServiceType(ServiceType serviceType) {
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

    @Transactional
    @Override
    public void setField(SetFieldRequest request) {
        Optional<ServiceAnnouncementEntity> repServiceAnnouncement = serviceAnnouncementRepository.findById(request.getId());
        if (repServiceAnnouncement.isPresent()) {
            ServiceAnnouncementEntity serviceAnnouncement = repServiceAnnouncement.get();
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userId.equals(serviceAnnouncement.getOwnerId())) {
                try {
                    Field field = ServiceAnnouncementEntity.class.getDeclaredField(request.getFieldName());
                    field.setAccessible(true);
                    field.set(serviceAnnouncement, request.getFieldValue());
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Поле не существует", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Поле не изменено из-за ошибки", e);
                }
            }
        } else throw new EntityNotFoundException("Объявление с таким id не найден");
    }

    @Transactional
    @Override
    public void update(ServiceAnnouncementDTO serviceAnnouncementDTO, ServiceAnnouncementEntity serviceAnnouncementEntity) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId.equals(serviceAnnouncementEntity.getOwnerId())) {
            ServiceAnnouncementMapper.INSTANCE.updateServiceAnnouncementFromDTO(serviceAnnouncementDTO, serviceAnnouncementEntity);
            serviceAnnouncementRepository.save(serviceAnnouncementEntity);
        }
    }
}
