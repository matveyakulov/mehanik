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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.dto.ServiceAnnouncementShowDTO;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.CarType;
import ru.neirodev.mehanik.enums.ServiceType;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarBrandService;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarTypeService;
import ru.neirodev.mehanik.service.ServiceAnnouncementPhotoService;
import ru.neirodev.mehanik.service.ServiceAnnouncementService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/serviceAnnouncements")
public class ServiceAnnouncementController {

    private final ServiceAnnouncementCarBrandService serviceAnnouncementCarBrandService;

    private final ServiceAnnouncementCarTypeService serviceAnnouncementCarTypeService;

    private final ServiceAnnouncementService serviceAnnouncementService;

    private final ServiceAnnouncementPhotoService serviceAnnouncementPhotoService;

    @Autowired
    public ServiceAnnouncementController(ServiceAnnouncementCarBrandService serviceAnnouncementCarBrandService,
                                         ServiceAnnouncementCarTypeService serviceAnnouncementCarTypeService,
                                         ServiceAnnouncementService serviceAnnouncementService,
                                         ServiceAnnouncementPhotoService serviceAnnouncementPhotoService) {
        this.serviceAnnouncementCarBrandService = serviceAnnouncementCarBrandService;
        this.serviceAnnouncementCarTypeService = serviceAnnouncementCarTypeService;
        this.serviceAnnouncementService = serviceAnnouncementService;
        this.serviceAnnouncementPhotoService = serviceAnnouncementPhotoService;
    }

    @GetMapping("/DTO")
    @Operation(summary = "Список объявлений")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ServiceAnnouncementShowDTO.class)))
    public List<ServiceAnnouncementShowDTO> getAllDTO(
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam(required = false) final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam(required = false) final Integer pageSize,
            @Parameter(description = "Тип сервиса")
            @RequestParam final ServiceType serviceType) {
        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > -1) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return serviceAnnouncementService.getAllDTOByServiceType(serviceType, pageable);
        }
        return serviceAnnouncementService.getAllDTOByServiceType(serviceType);
    }

    @Operation(summary = "Получение объявления по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartAnnouncementEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найден")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final Long id) {
        Optional<ServiceAnnouncementEntity> serviceAnnouncement = serviceAnnouncementService.findById(id);
        if (serviceAnnouncement.isPresent()) {
            return ResponseEntity.ok().body(serviceAnnouncement.get());
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Создание обновления объявления")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody final ServiceAnnouncementEntity serviceAnnouncement) {
        try {
            return ResponseEntity.ok().body(serviceAnnouncementService.save(serviceAnnouncement));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление нескольких полей объявления(обновятся все, которые не null у входящего объекта)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping("")
    public ResponseEntity<?> update(
            @RequestBody final ServiceAnnouncementDTO serviceAnnouncementDTO) {
        try {
            Optional<ServiceAnnouncementEntity> repServiceAnnouncement = serviceAnnouncementService.findById(serviceAnnouncementDTO.getId());
            if (repServiceAnnouncement.isPresent()) {
                serviceAnnouncementService.update(serviceAnnouncementDTO, repServiceAnnouncement.get());
                return ResponseEntity.accepted().build();
            }
            return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление одного поля")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не существует")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не изменено из-за ошибки")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @PutMapping("/field")
    public ResponseEntity<?> update(@RequestBody final SetFieldRequest request) {
        try {
            serviceAnnouncementService.setField(request);
            return ResponseEntity.accepted().build();
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление объявления(если текущий пользователь создавал его. Все фотки, марки и типы ТС удалятся автоматически)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable final Long id) {
        Optional<ServiceAnnouncementEntity> serviceAnnouncement = serviceAnnouncementService.findById(id);
        if (serviceAnnouncement.isPresent()) {
            Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userId.equals(serviceAnnouncement.get().getOwnerId())) {
                try {
                    serviceAnnouncementService.delete(serviceAnnouncement.get());
                    serviceAnnouncementPhotoService.deleteByAnnouncementId(id);
                    serviceAnnouncementCarBrandService.deleteByAnnouncementId(id);
                    serviceAnnouncementCarTypeService.deleteByAnnouncementId(id);
                    return ResponseEntity.ok().build();
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().build();
                }
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление фотографий в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/photos")
    public ResponseEntity<?> addPhotos(@PathVariable final Long id, @RequestBody final List<String> photos) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementPhotoService.save(id, photos);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление фотографий в объявлении (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/photos")
    public ResponseEntity<?> deletePhotos(@PathVariable final Long id, @RequestBody final List<String> photos) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementPhotoService.delete(id, photos);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление типов ТС в объявление (если он один, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/types")
    public ResponseEntity<?> addTypes(@PathVariable final Long id, @RequestBody final List<CarType> types) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementCarTypeService.save(id, types);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление типов ТС в объявлении (если он один, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/types")
    public ResponseEntity<?> deleteTypes(@PathVariable final Long id, @RequestBody final List<CarType> types) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementCarTypeService.delete(id, types);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление марок ТС в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/brands")
    public ResponseEntity<?> addBrands(@PathVariable final Long id, @RequestBody final List<String> brands) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementCarBrandService.save(id, brands);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление марок ТС в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Объявление с таким id не найдено")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/brands")
    public ResponseEntity<?> deleteBrands(@PathVariable final Long id, @RequestBody final List<String> brands) {
        if (serviceAnnouncementService.existsById(id)) {
            try {
                serviceAnnouncementCarBrandService.delete(id, brands);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return new ResponseEntity<>("Объявление с таким id не найдено", NOT_FOUND);
    }
}
