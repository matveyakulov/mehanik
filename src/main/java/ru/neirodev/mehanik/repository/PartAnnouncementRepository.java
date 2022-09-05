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

    @Query(value = "SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.model," +
            "pa.generation, pa.nameOfPart, pa.numberOfPart, pa.city, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive, " +
            "pa.longitude, pa.latitude) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive, Long userId);

    @Query(value = "SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.model," +
            "pa.generation, pa.nameOfPart, pa.numberOfPart, pa.city, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive, " +
            "pa.longitude, pa.latitude) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.archive = :archive OR :archive IS NULL AND pa.ownerId = :userId")
    List<PartAnnouncementDTO> getAllCurrentDTO(Boolean archive, Long userId);

    @Query(value = "SELECT new ru.neirodev.mehanik.dto.PartAnnouncementDTO(pa.id, pa.type, pa.brand, pa.model, " +
            "pa.generation, pa.nameOfPart, pa.numberOfPart, pa.city, pa.photo, pa.address, pa.price, pa.dateCreate, pa.archive, " +
            "pa.longitude, pa.latitude) " +
            "FROM PartAnnouncementEntity pa " +
            "WHERE pa.type IN :types AND pa.brand IN :brands AND (pa.nameOfPart = :nameOfPart OR :nameOfPart IS NULL) " +
            "AND (pa.price >= :startPrice OR :startPrice IS NULL) AND (pa.price <= :endPrice OR :endPrice IS NULL) " +
            "AND (pa.condition = :condition OR :condition IS NULL) AND (pa.original = :original OR :original IS NULL) " +
            "AND (pa.isCompany = :isCompany OR :isCompany IS NULL) AND pa.archive = false")
    List<PartAnnouncementDTO> getAllDTO(List<String> types, List<String> brands, String nameOfPart, Integer startPrice,
                                        Integer endPrice, Boolean condition, Boolean original, Boolean isCompany, Pageable pageable);
}
