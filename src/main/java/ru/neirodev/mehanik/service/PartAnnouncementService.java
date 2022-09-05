package ru.neirodev.mehanik.service;

import org.springframework.data.domain.Pageable;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;

import java.util.List;
import java.util.Optional;

public interface PartAnnouncementService {

    List<PartAnnouncementDTO> getAllCurrentDTO(Boolean archive);

    List<PartAnnouncementDTO> getAllCurrentDTO(Pageable pageable, Boolean archive);

    Optional<PartAnnouncementEntity> findById(Long id);

    void addToArchive(Long id);

    PartAnnouncementEntity save(PartAnnouncementEntity partAnnouncementEntity);

    void delete(PartAnnouncementEntity partAnnouncementEntity);

    List<PartAnnouncementDTO> getAllDTO(Double userLatitude, Double userLongitude, Double radius, String city,
                                        List<String> types, List<String> brands,
                                        String nameOfPart, Integer startPrice, Integer endPrice, Boolean aBoolean,
                                        Boolean condition, Boolean isCompany, Integer pageNum, Integer pageSize);

    void update(PartAnnouncementEntity partAnnouncementEntity);

    boolean existsById(Long id);
}
