package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.entity.ServiceAnnouncementCarTypeEntity;
import ru.neirodev.mehanik.enums.CarType;
import ru.neirodev.mehanik.repository.ServiceAnnouncementCarTypeRepository;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarTypeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceAnnouncementCarTypeServiceImpl implements ServiceAnnouncementCarTypeService {

    private final ServiceAnnouncementCarTypeRepository serviceAnnouncementCarTypeRepository;

    @Autowired
    public ServiceAnnouncementCarTypeServiceImpl(ServiceAnnouncementCarTypeRepository serviceAnnouncementCarTypeRepository) {
        this.serviceAnnouncementCarTypeRepository = serviceAnnouncementCarTypeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ServiceAnnouncementCarTypeEntity> findById(Long id) {
        return serviceAnnouncementCarTypeRepository.findById(id);
    }

    @Transactional
    @Override
    public void save(ServiceAnnouncementCarTypeEntity serviceAnnouncementCarTypeEntity) {
        serviceAnnouncementCarTypeRepository.save(serviceAnnouncementCarTypeEntity);
    }

    @Transactional
    @Override
    public void delete(ServiceAnnouncementCarTypeEntity serviceAnnouncementCarTypeEntity) {
        serviceAnnouncementCarTypeRepository.delete(serviceAnnouncementCarTypeEntity);
    }

    @Transactional
    @Override
    public void deleteByAnnouncementId(Long id) {
        serviceAnnouncementCarTypeRepository.deleteAllByServiceAnnouncementId(id);
    }

    @Transactional
    @Override
    public void delete(Long id, List<CarType> types) {
        types.forEach(type -> serviceAnnouncementCarTypeRepository.deleteByServiceAnnouncementIdAndType(id, type));
    }

    @Transactional
    @Override
    public void save(Long id, List<CarType> types) {
        List<ServiceAnnouncementCarTypeEntity> entities = types.stream()
                .map(type -> new ServiceAnnouncementCarTypeEntity(id, type)).collect(Collectors.toList());
        serviceAnnouncementCarTypeRepository.saveAll(entities);
    }
}
