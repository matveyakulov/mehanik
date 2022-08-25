package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncement;
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
        return partAnnouncementRepository.getAllDTO(archive);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(Pageable pageable, Boolean archive) {
        return partAnnouncementRepository.getAllDTO(pageable, archive);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PartAnnouncement> findById(Long id) {
        return partAnnouncementRepository.findById(id);
    }

    @Transactional
    @Override
    public void addToArchive(Long id) {
        PartAnnouncement partAnnouncement = partAnnouncementRepository.getReferenceById(id);
        partAnnouncement.setArchive(!partAnnouncement.getArchive());
    }

    @Transactional
    @Override
    public PartAnnouncement save(PartAnnouncement partAnnouncement) {
        return partAnnouncementRepository.save(partAnnouncement);
    }

    @Transactional
    @Override
    public void delete(PartAnnouncement partAnnouncement) {
        partAnnouncementRepository.delete(partAnnouncement);
    }
}
