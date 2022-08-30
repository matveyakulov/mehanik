package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.repository.PartAnnouncementRepository;
import ru.neirodev.mehanik.service.PartAnnouncementService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partAnnouncementRepository.getAllCurrentDTO(archive, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllCurrentDTO(Pageable pageable, Boolean archive) {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    public void delete(PartAnnouncementEntity partAnnouncementEntity) {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (id.equals(partAnnouncementEntity.getOwnerId())) {
            partAnnouncementRepository.delete(partAnnouncementEntity);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(
            String city, List<String> types, List<String> brands, String nameOfPart, Integer startPrice, Integer endPrice,
            Boolean condition, Boolean isCompany, Boolean original, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "dateCreate");
        List<PartAnnouncementDTO> dtos = partAnnouncementRepository.getAllDTO(types, brands, nameOfPart,
                startPrice, endPrice, condition, original, isCompany, pageable);
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
