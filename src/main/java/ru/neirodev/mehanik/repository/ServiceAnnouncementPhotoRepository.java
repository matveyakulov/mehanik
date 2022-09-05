package ru.neirodev.mehanik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.entity.ServiceAnnouncementPhotoEntity;

@Repository
public interface ServiceAnnouncementPhotoRepository extends JpaRepository<ServiceAnnouncementPhotoEntity, Long> {

    void deleteAllByServiceAnnouncementId(Long serviceAnnouncementId);

    void deleteByServiceAnnouncementIdAndPhoto(Long serviceAnnouncementId, String photo);
}
