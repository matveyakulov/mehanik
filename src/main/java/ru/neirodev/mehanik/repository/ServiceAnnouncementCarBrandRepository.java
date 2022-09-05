package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.ServiceAnnouncementCarBrandEntity;

@Repository
public interface ServiceAnnouncementCarBrandRepository extends JpaRepository<ServiceAnnouncementCarBrandEntity, Long> {

    void deleteAllByServiceAnnouncementId(Long id);

    void deleteByServiceAnnouncementIdAndBrand(Long id, String brand);
}
