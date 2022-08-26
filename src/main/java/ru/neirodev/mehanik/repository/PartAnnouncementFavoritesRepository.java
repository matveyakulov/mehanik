package ru.neirodev.mehanik.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementFavoriteEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartAnnouncementFavoritesRepository extends JpaRepository<PartAnnouncementFavoriteEntity, Long> {

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation, pa.model, pa.nameOfPart, " +
            "pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "JOIN PartAnnouncementFavoriteEntity paf ON pa.id = paf.partsAnnouncementId " +
            "WHERE paf.userId = :id")
    List<PartAnnouncementDTO> getAllDTO(Long id);

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation, pa.model, pa.nameOfPart, " +
            "pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "JOIN PartAnnouncementFavoriteEntity paf ON pa.id = paf.partsAnnouncementId " +
            "WHERE paf.userId = :id")
    List<PartAnnouncementDTO> getAllDTO(Long id, Pageable pageable);

    Optional<PartAnnouncementFavoriteEntity> findByUserIdAndPartsAnnouncementId(Long userId, Long partsAnnouncementId);
}
