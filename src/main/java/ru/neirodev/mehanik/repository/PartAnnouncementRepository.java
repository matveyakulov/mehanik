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
            "pa.model, pa.nameOfPart, pa.numberOfPart, pa.photo, pa.address, pa.city, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllCurrentDTO(Pageable pageable, Boolean archive, Long userId);

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation," +
            "pa.model, pa.nameOfPart, pa.numberOfPart, pa.photo, pa.address, pa.city, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllCurrentDTO(Boolean archive, Long userId);

    @Query("SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.generation, " +
            "pa.model, pa.nameOfPart, pa.numberOfPart, pa.photo, pa.address, pa.city, pa.price, pa.dateCreate, pa.archive) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.type IN :types AND pa.brand IN :brands AND pa.numberOfPart = :nameOfPart " +
            "AND pa.price BETWEEN :startPrice AND :endPrice AND (pa.condition = :condition OR :condition IS NULL) " +
            "AND (pa.original = :original OR :original IS NULL) AND (pa.isCompany = :isCompany OR :isCompany IS NULL) ")
    List<PartAnnouncementDTO> getAllDTO(List<String> types, List<String> brands, String nameOfPart, Integer startPrice,
                                        Integer endPrice, Boolean condition, Boolean original, Boolean isCompany, Pageable pageable);
}
