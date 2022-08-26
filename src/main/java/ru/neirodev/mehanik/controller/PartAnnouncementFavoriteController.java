package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.PartAnnouncementDTO;
import ru.neirodev.mehanik.service.PartAnnouncementFavoriteService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/partAnnouncementFavorites")
public class PartAnnouncementFavoriteController {

    private final PartAnnouncementFavoriteService partAnnouncementFavoriteService;

    @Autowired
    public PartAnnouncementFavoriteController(PartAnnouncementFavoriteService partAnnouncementFavoriteService) {
        this.partAnnouncementFavoriteService = partAnnouncementFavoriteService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Список объявлений о продаже в избранном у текущего пользователя(только авторизованного)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @GetMapping
    public List<PartAnnouncementDTO> getAllDTO(
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam(required = false) final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam(required = false) final Integer pageSize) {
        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > -1) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return partAnnouncementFavoriteService.getAllDTO(pageable);
        }
        return partAnnouncementFavoriteService.getAllDTO();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление объявления о продаже в избранное текущим пользователем(авторизованным)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public ResponseEntity<?> save(@RequestParam final Long partAnnouncementId) {
        try {
            partAnnouncementFavoriteService.save(partAnnouncementId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление объявления о продаже из избранного текущим пользователем(авторизованным)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam final Long partAnnouncementId) {
        try {
            partAnnouncementFavoriteService.delete(partAnnouncementId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}