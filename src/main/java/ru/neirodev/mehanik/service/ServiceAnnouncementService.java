package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceAnnouncementService {

    List<ServiceAnnouncementDTO> getAllDTOByServiceType(ServiceType serviceType, Pageable pageable);
    List<ServiceAnnouncementDTO> getAllDTOByServiceType(ServiceType serviceType);

    Optional<ServiceAnnouncementEntity> findById(Long id);

    boolean existsById(Long id);

    ServiceAnnouncementEntity save(ServiceAnnouncementEntity serviceAnnouncement);
    void delete(ServiceAnnouncementEntity serviceAnnouncementEntity);
}
