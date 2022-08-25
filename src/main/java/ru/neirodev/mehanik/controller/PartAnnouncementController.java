package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncement;
import ru.neirodev.mehanik.service.PartAnnouncementService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/partAnnouncements")
public class PartAnnouncementController {

    private final PartAnnouncementService partAnnouncementService;

    @Autowired
    public PartAnnouncementController(PartAnnouncementService partAnnouncementService) {
        this.partAnnouncementService = partAnnouncementService;
    }

    @GetMapping("/DTO")
    public List<PartAnnouncementDTO> getAllDTO(
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam(required = false) final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam(required = false) final Integer pageSize,
            @Parameter(description = "Архивность, если null, то вернутся записи и архивные и нет")
            @RequestParam(required = false) final Boolean archive) {
        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > -1) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return partAnnouncementService.getAllDTO(pageable, archive);
        }
        return partAnnouncementService.getAllDTO(archive);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getById(@PathVariable final Long id) {
        Optional<PartAnnouncement> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            return ResponseEntity.ok().body(partAnnouncement.get());
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody final PartAnnouncement partAnnouncement){
        try {
            return ResponseEntity.ok().body(partAnnouncementService.save(partAnnouncement));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(@PathVariable final Long id) {
        Optional<PartAnnouncement> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            partAnnouncementService.delete(partAnnouncement.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<?> addToArchive(@PathVariable final Long id){
        Optional<PartAnnouncement> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            partAnnouncementService.addToArchive(id);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

}
