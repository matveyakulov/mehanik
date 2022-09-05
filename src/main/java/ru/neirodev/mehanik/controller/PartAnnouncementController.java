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

    @Operation(summary = "Список объявлений о продаже с фильтрацией(без радиуса)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementDTO.class)))
    @GetMapping
    public List<PartAnnouncementDTO> getAllDTO(
            @Parameter(description = "Широта пользователя(обязателен, если нужна фильтрация по радиусу)")
            @RequestParam(required = false) final Double userLatitude,
            @Parameter(description = "Долгота пользователя(обязателен, если нужна фильтрация по радиусу)")
            @RequestParam(required = false) final Double userLongitude,
            @Parameter(description = "Радиус поиска")
            @RequestParam(required = false) final Double radius,
            @Parameter(description = "Город, который в приоритете (не передавать, если не нужен приоритет)")
            @RequestParam(required = false) final String city,
            @Parameter(description = "Типы ТС", required = true)
            @RequestParam final List<String> types,
            @Parameter(description = "Марки", required = true)
            @RequestParam final List<String> brands,
            @Parameter(description = "Название запчасти")
            @RequestParam(required = false) final String nameOfPart,
            @Parameter(description = "Начальная цена")
            @RequestParam(required = false) final Integer startPrice,
            @Parameter(description = "Конечная цена")
            @RequestParam(required = false) final Integer endPrice,
            @Parameter(description = "Состояние, если неважно, то не передавай")
            @RequestParam(required = false) final Boolean condition,
            @Parameter(description = "Кто продает, если неважно, то не передавай")
            @RequestParam(required = false) final Boolean isCompany,
            @Parameter(description = "Оригинальность, если неважно, то не передавай")
            @RequestParam(required = false) final Boolean original,
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam final Integer pageSize) {
        return partAnnouncementService.getAllDTO(userLatitude, userLongitude, radius, city, types, brands, nameOfPart,
                startPrice, endPrice, condition, isCompany, original, pageNum, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Список объявлений о продаже у текущего пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementDTO.class)))
    @GetMapping("/me")
    public List<PartAnnouncementDTO> getAllDTO(
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam(required = false) final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam(required = false) final Integer pageSize,
            @Parameter(description = "Архивность, если null, то вернутся записи и архивные и нет")
            @RequestParam(required = false) final Boolean archive) {
        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > -1) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return partAnnouncementService.getAllCurrentDTO(pageable, archive);
        }
        return partAnnouncementService.getAllCurrentDTO(archive);
    }

    @Operation(summary = "Получение объявления по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найден")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final Long id) {
        Optional<PartAnnouncementEntity> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()) {
            return ResponseEntity.ok().body(partAnnouncement.get());
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Создание объявления")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody final PartAnnouncementEntity partAnnouncementEntity) {
        try {
            return ResponseEntity.ok().body(partAnnouncementService.save(partAnnouncementEntity));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление объявления")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody final PartAnnouncementEntity partAnnouncementEntity) {
        try {
            partAnnouncementService.update(partAnnouncementEntity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление объявления(если текущий пользователь создавал его)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable final Long id) {
        Optional<PartAnnouncementEntity> partAnnouncement = partAnnouncementService.findById(id);
        if (partAnnouncement.isPresent()) {
            partAnnouncementService.delete(partAnnouncement.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Архивирование / разархивирование объявления(если текущий пользователь создавал его)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @PostMapping("/{id}/archive")
    public ResponseEntity<?> addToArchive(@PathVariable final Long id) {
        if (partAnnouncementService.existsById(id)) {
            partAnnouncementService.addToArchive(id);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

}
