package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementFavoriteEntity;

import java.util.List;
import java.util.Optional;

public interface PartAnnouncementFavoriteService {

    List<PartAnnouncementDTO> getAllDTO();
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable);
    void save(Long partAnnouncementId);
    void delete(Long partAnnouncementId);

    Optional<PartAnnouncementFavoriteEntity> findById(Long id);
}
