package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.ServiceAnnouncementDTO;
import ru.neirodev.mehanik.dto.ServiceAnnouncementShowDTO;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.entity.PartAnnouncementEntity;
import ru.neirodev.mehanik.entity.ServiceAnnouncementEntity;
import ru.neirodev.mehanik.enums.CarType;
import ru.neirodev.mehanik.enums.ServiceType;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarBrandService;
import ru.neirodev.mehanik.service.ServiceAnnouncementCarTypeService;
import ru.neirodev.mehanik.service.ServiceAnnouncementPhotoService;
import ru.neirodev.mehanik.service.ServiceAnnouncementService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/serviceAnnouncements")
public class ServiceAnnouncementController {

    private final ServiceAnnouncementCarBrandService serviceAnnouncementCarBrandService;

    private final ServiceAnnouncementCarTypeService serviceAnnouncementCarTypeService;

    private final ServiceAnnouncementService serviceAnnouncementService;

    private final ServiceAnnouncementPhotoService serviceAnnouncementPhotoService;

    private final String NOT_FOUND_MESSAGE = "Объявление с таким id не найдено";

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
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @GetMapping("/{id}")
    public ServiceAnnouncementEntity getById(@PathVariable final Long id) {
        return serviceAnnouncementService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Создание обновления объявления")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public ServiceAnnouncementEntity save(@RequestBody final ServiceAnnouncementEntity serviceAnnouncement) {
        return serviceAnnouncementService.save(serviceAnnouncement);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление нескольких полей объявления(обновятся все, которые не null у входящего объекта)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping("")
    @ResponseStatus(ACCEPTED)
    public void update(@RequestBody final ServiceAnnouncementDTO serviceAnnouncementDTO) {
        ServiceAnnouncementEntity repServiceAnnouncement = serviceAnnouncementService.findById(serviceAnnouncementDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        serviceAnnouncementService.update(serviceAnnouncementDTO, repServiceAnnouncement);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление одного поля")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не существует")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не изменено из-за ошибки")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @PutMapping("/field")
    @ResponseStatus(ACCEPTED)
    public void update(@RequestBody final SetFieldRequest request) {
        serviceAnnouncementService.setField(request);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление объявления(если текущий пользователь создавал его. Все фотки, марки и типы ТС удалятся автоматически)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        ServiceAnnouncementEntity serviceAnnouncement = serviceAnnouncementService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        Long userId = JwtTokenUtil.getUserIdFromPrincipal();
        if (userId.equals(serviceAnnouncement.getOwnerId())) {
            serviceAnnouncementService.deleteById(id);
            serviceAnnouncementPhotoService.deleteByAnnouncementId(id);
            serviceAnnouncementCarBrandService.deleteByAnnouncementId(id);
            serviceAnnouncementCarTypeService.deleteByAnnouncementId(id);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление фотографий в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/photos")
    public void addPhotos(@PathVariable final Long id, @RequestBody final List<String> photos) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementPhotoService.save(id, photos);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление фотографий в объявлении (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/photos")
    public void deletePhotos(@PathVariable final Long id, @RequestBody final List<String> photos) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementPhotoService.delete(id, photos);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление типов ТС в объявление (если он один, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/types")
    public void addTypes(@PathVariable final Long id, @RequestBody final List<CarType> types) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementCarTypeService.save(id, types);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление типов ТС в объявлении (если он один, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/types")
    public void deleteTypes(@PathVariable final Long id, @RequestBody final List<CarType> types) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementCarTypeService.delete(id, types);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление марок ТС в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/brands")
    public void addBrands(@PathVariable final Long id, @RequestBody final List<String> brands) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementCarBrandService.save(id, brands);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Удаление марок ТС в объявление (если она одна, то массив с одним элементом)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}/brands")
    public void deleteBrands(@PathVariable final Long id, @RequestBody final List<String> brands) {
        if (serviceAnnouncementService.existsById(id)) {
            serviceAnnouncementCarBrandService.delete(id, brands);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}
