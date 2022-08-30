package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;

import java.util.List;
import java.util.Optional;

public interface PartAnnouncementService {

    List<PartAnnouncementDTO> getAllDTO(Boolean archive);
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive);

    Optional<PartAnnouncementEntity> findById(Long id);

    void addToArchive(Long id);

    PartAnnouncementEntity save(PartAnnouncementEntity partAnnouncementEntity);
    void delete(PartAnnouncementEntity partAnnouncementEntity);
}
