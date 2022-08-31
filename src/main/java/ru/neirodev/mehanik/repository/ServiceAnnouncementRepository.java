package ru.neirodev.mehanik.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.ServiceType;

import java.util.List;

@Repository
public interface ServiceAnnouncementRepository extends JpaRepository<ServiceAnnouncementEntity, Long> {

    @Query("SELECT new ru.neirodev.mehanik.dto.ServiceAnnouncementDTO(sa.id, sa.companyName, sa.address, sa.rating) " +
            "FROM ServiceAnnouncementEntity sa WHERE sa.service = :serviceType")
    List<ServiceAnnouncementDTO> findAllByService(ServiceType serviceType, Pageable pageable);

    @Query("SELECT new ru.neirodev.mehanik.dto.ServiceAnnouncementDTO(sa.id, sa.companyName, sa.address, sa.rating) " +
            "FROM ServiceAnnouncementEntity sa WHERE sa.service = :serviceType")
    List<ServiceAnnouncementDTO> findAllByService(ServiceType serviceType);
}
