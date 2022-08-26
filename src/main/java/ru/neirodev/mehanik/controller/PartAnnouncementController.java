package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.service.PartAnnouncementService;

import javax.servlet.http.HttpServletResponse;
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

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Список объявлений о продаже у текущего пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementDTO.class)))
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

    @Operation(summary = "Получение объявления по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @GetMapping("/{id}")
    private ResponseEntity<?> getById(@PathVariable final Long id) {
        Optional<PartAnnouncementEntity> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            return ResponseEntity.ok().body(partAnnouncement.get());
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Создание объявления")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody final PartAnnouncementEntity partAnnouncementEntity){
        try {
            return ResponseEntity.ok().body(partAnnouncementService.save(partAnnouncementEntity));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление объявления(если текущий пользователь создавал его)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(@PathVariable final Long id) {
        Optional<PartAnnouncementEntity> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            partAnnouncementService.delete(partAnnouncement.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Архивирование / разархивирование объявления(если текущий пользователь создавал его)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @PostMapping("/{id}/archive")
    public ResponseEntity<?> addToArchive(@PathVariable final Long id){
        Optional<PartAnnouncementEntity> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()){
            partAnnouncementService.addToArchive(id);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено",  NOT_FOUND);
    }

}
