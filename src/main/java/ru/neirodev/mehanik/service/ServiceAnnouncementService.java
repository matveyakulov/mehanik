package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.dto.ServiceAnnouncementShowDTO;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceAnnouncementService {

    List<ServiceAnnouncementShowDTO> getAllDTOByServiceType(ServiceType serviceType, Pageable pageable);
    List<ServiceAnnouncementShowDTO> getAllDTOByServiceType(ServiceType serviceType);

    Optional<ServiceAnnouncementEntity> findById(Long id);

    boolean existsById(Long id);

    ServiceAnnouncementEntity save(ServiceAnnouncementEntity serviceAnnouncement);
    void delete(ServiceAnnouncementEntity serviceAnnouncementEntity);

    void update(ServiceAnnouncementDTO serviceAnnouncementDTO, ServiceAnnouncementEntity serviceAnnouncement);

    void setField(SetFieldRequest request);

    void deleteById(Long id);
}
