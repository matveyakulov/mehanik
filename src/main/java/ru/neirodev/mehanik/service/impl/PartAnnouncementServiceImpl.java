package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.repository.PartAnnouncementRepository;
import ru.neirodev.mehanik.service.PartAnnouncementService;

import java.util.List;
import java.util.Optional;

@Service
public class PartAnnouncementServiceImpl implements PartAnnouncementService {

    private final PartAnnouncementRepository partAnnouncementRepository;

    @Autowired
    public PartAnnouncementServiceImpl(PartAnnouncementRepository partAnnouncementRepository) {
        this.partAnnouncementRepository = partAnnouncementRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(Boolean archive) {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partAnnouncementRepository.getAllDTO(archive, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive) {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partAnnouncementRepository.getAllDTO(pageable, archive, id);
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
}
