package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.entity.ServiceAnnouncementCarBrandEntity;
import ru.neirodev.mehanik.repository.ServiceAnnouncementCarBrandRepository;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarBrandService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceAnnouncementCarBrandServiceImpl implements ServiceAnnouncementCarBrandService {

    private final ServiceAnnouncementCarBrandRepository serviceAnnouncementCarBrandRepository;

    @Autowired
    public ServiceAnnouncementCarBrandServiceImpl(ServiceAnnouncementCarBrandRepository serviceAnnouncementCarBrandRepository) {
        this.serviceAnnouncementCarBrandRepository = serviceAnnouncementCarBrandRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ServiceAnnouncementCarBrandEntity> findById(Long id) {
        return serviceAnnouncementCarBrandRepository.findById(id);
    }

    @Transactional
    @Override
    public void save(ServiceAnnouncementCarBrandEntity serviceAnnouncementCarBrandEntity) {
        serviceAnnouncementCarBrandRepository.save(serviceAnnouncementCarBrandEntity);
    }

    @Transactional
    @Override
    public void delete(ServiceAnnouncementCarBrandEntity serviceAnnouncementCarBrandEntity) {
        serviceAnnouncementCarBrandRepository.delete(serviceAnnouncementCarBrandEntity);
    }

    @Transactional
    @Override
    public void deleteByAnnouncementId(Long id) {
        serviceAnnouncementCarBrandRepository.deleteAllByServiceAnnouncementId(id);
    }

    @Transactional
    @Override
    public void save(Long id, List<String> brands) {
        List<ServiceAnnouncementCarBrandEntity> entities = brands.stream()
                .map(brand -> new ServiceAnnouncementCarBrandEntity(id, brand)).collect(Collectors.toList());
        serviceAnnouncementCarBrandRepository.saveAll(entities);
    }

    @Transactional
    @Override
    public void delete(Long id, List<String> brands) {
        for (String brand : brands) {
            serviceAnnouncementCarBrandRepository.deleteByServiceAnnouncementIdAndBrand(id, brand);
        }
    }
}
