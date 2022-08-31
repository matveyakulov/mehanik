package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.ServiceAnnouncementCarTypeEntity;
import ru.neirodev.mehanik.enums.CarType;

@Repository
public interface ServiceAnnouncementCarTypeRepository extends JpaRepository<ServiceAnnouncementCarTypeEntity, Long> {

    void deleteAllByServiceAnnouncementId(Long id);

    void  deleteByServiceAnnouncementIdAndType(Long id, CarType type);
}
