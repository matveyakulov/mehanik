package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.repository.PartAnnouncementRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.PartAnnouncementService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.neirodev.mehanik.util.GPSUtils.getDistance;

@Service
public class PartAnnouncementServiceImpl implements PartAnnouncementService {

    private final PartAnnouncementRepository partAnnouncementRepository;

    @Autowired
    public PartAnnouncementServiceImpl(PartAnnouncementRepository partAnnouncementRepository) {
        this.partAnnouncementRepository = partAnnouncementRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllCurrentDTO(Boolean archive) {
        Long id = JwtTokenUtil.getUserIdFromPrincipal();
        return partAnnouncementRepository.getAllCurrentDTO(archive, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllCurrentDTO(Pageable pageable, Boolean archive) {
        Long id = JwtTokenUtil.getUserIdFromPrincipal();
        return partAnnouncementRepository.getAllCurrentDTO(pageable, archive, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PartAnnouncementEntity> findById(Long id) {
        return partAnnouncementRepository.findById(id);
    }

    @Transactional
    @Override
    public void addToArchive(Long id) {
        PartAnnouncementEntity partAnnouncementEntity = partAnnouncementRepository.getReferenceById(id);
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        if (userId.equals(partAnnouncementEntity.getOwnerId())) {
            partAnnouncementEntity.setArchive(!partAnnouncementEntity.getArchive());
        }
    }

    @Transactional
    @Override
    public PartAnnouncementEntity save(PartAnnouncementEntity partAnnouncementEntity) {
        return partAnnouncementRepository.save(partAnnouncementEntity);
    }

    @Transactional
    @Override
    public void update(PartAnnouncementEntity partAnnouncementEntity) {
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        if (userId.equals(partAnnouncementEntity.getOwnerId())) {
            partAnnouncementRepository.save(partAnnouncementEntity);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return partAnnouncementRepository.existsById(id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        partAnnouncementRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(PartAnnouncementEntity partAnnouncementEntity) {
        Long id = JwtTokenUtil.getUserIdFromPrincipal();
        if (id.equals(partAnnouncementEntity.getOwnerId())) {
            partAnnouncementRepository.delete(partAnnouncementEntity);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(
            Double userLatitude, Double userLongitude, Double radius, String city, List<String> types, List<String> brands, String nameOfPart, Integer startPrice,
            Integer endPrice, Boolean condition, Boolean isCompany, Boolean original, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "dateCreate");
        List<PartAnnouncementDTO> dtos = partAnnouncementRepository.getAllDTO(types, brands, nameOfPart,
                startPrice, endPrice, condition, original, isCompany, pageable);
        if (radius != null && userLongitude != null && userLatitude != null) {
            dtos = dtos.stream()
                    .filter(dto -> getDistance(userLongitude, userLatitude, dto.getLongitude(), dto.getLatitude()) <= radius)
                    .collect(Collectors.toList());
        }
        if (city != null) {
            LinkedList<PartAnnouncementDTO> partAnnouncementDTOS = dtos.stream()
                    .filter(dto -> dto.getCity().equals(city))
                    .collect(Collectors.toCollection(LinkedList::new));
            dtos.removeAll(partAnnouncementDTOS);
            partAnnouncementDTOS.addAll(dtos);
            return partAnnouncementDTOS;
        }
        return dtos;
    }
}
