package ru.neirodev.mehanik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementFavoriteEntity;
import ru.neirodev.mehanik.repository.PartAnnouncementFavoritesRepository;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.PartAnnouncementFavoriteService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PartAnnouncementFavoriteServiceImpl implements PartAnnouncementFavoriteService {

    private final PartAnnouncementFavoritesRepository partAnnouncementFavoritesRepository;

    @Autowired
    public PartAnnouncementFavoriteServiceImpl(PartAnnouncementFavoritesRepository partAnnouncementFavoritesRepository) {
        this.partAnnouncementFavoritesRepository = partAnnouncementFavoritesRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO() {
        Long id = JwtTokenUtil.getUserIdFromPrincipal();
        return partAnnouncementFavoritesRepository.getAllDTO(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartAnnouncementDTO> getAllDTO(Pageable pageable) {
        Long id = JwtTokenUtil.getUserIdFromPrincipal();
        return partAnnouncementFavoritesRepository.getAllDTO(id, pageable);
    }

    @Transactional
    @Override
    public void save(Long partAnnouncementId) {
        PartAnnouncementFavoriteEntity partAnnouncementFavoriteEntity = new PartAnnouncementFavoriteEntity();
        partAnnouncementFavoriteEntity.setPartsAnnouncementId(partAnnouncementId);
        partAnnouncementFavoritesRepository.save(partAnnouncementFavoriteEntity);
    }

    @Transactional
    @Override
    public void delete(Long partAnnouncementId) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<PartAnnouncementFavoriteEntity> partAnnouncementFavorite =
                partAnnouncementFavoritesRepository.findByUserIdAndPartsAnnouncementId(userId, partAnnouncementId);
        partAnnouncementFavorite.orElseThrow(EntityNotFoundException::new);
        partAnnouncementFavoritesRepository.delete(partAnnouncementFavorite.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<PartAnnouncementFavoriteEntity> findById(Long id) {
        return partAnnouncementFavoritesRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(Long partAnnouncementId) {
        partAnnouncementFavoritesRepository.deleteById(partAnnouncementId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long partAnnouncementId) {
        return partAnnouncementFavoritesRepository.existsById(partAnnouncementId);
    }
}