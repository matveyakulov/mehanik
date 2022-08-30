package ru.neirodev.mehanik.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;

import java.util.List;

@Repository
public interface PartAnnouncementRepository extends JpaRepository<PartAnnouncementEntity, Long> {

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation," +
            "pa.model, pa.nameOfPart, pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive, Long userId);

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation," +
            "pa.model, pa.nameOfPart, pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllDTO(Boolean archive, Long userId);
}
