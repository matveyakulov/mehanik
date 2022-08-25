package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncement;

import java.util.List;
import java.util.Optional;

public interface PartAnnouncementService {

    List<PartAnnouncementDTO> getAllDTO(Boolean archive);
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive);

    Optional<PartAnnouncement> findById(Long id);

    void addToArchive(Long id);

    PartAnnouncement save(PartAnnouncement partAnnouncement);
    void delete(PartAnnouncement partAnnouncement);
}
