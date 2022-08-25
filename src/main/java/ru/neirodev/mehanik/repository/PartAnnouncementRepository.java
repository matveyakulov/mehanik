package ru.neirodev.mehanik.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncement;

import java.util.List;

@Repository
public interface PartAnnouncementRepository extends JpaRepository<PartAnnouncement, Long> {

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation, pa.model, pa.nameOfPart, " +
            "pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncement pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL")
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive);

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation, pa.model, pa.nameOfPart, " +
            "pa.numberOfPart, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncement pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL")
    List<PartAnnouncementDTO> getAllDTO(Boolean archive);
}
