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
import ru.neirodev.mehanik.entity.CarEntity;
import ru.neirodev.mehanik.service.CarService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cars")
@PreAuthorize("hasAnyAuthority('USER')")
public class CarController {

    private final CarService carService;

    private final String NOT_FOUND_MESSAGE = "Машина с таким id не найдена";

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Список список машин у текущего пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarEntity.class)))
    @GetMapping
    public List<CarEntity> getAll(
            @Parameter(description = "Номер страницы(с 0)")
            @RequestParam(required = false) final Integer pageNum,
            @Parameter(description = "Размер страницы(с 1)")
            @RequestParam(required = false) final Integer pageSize) {
        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > -1) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            return carService.getAll(pageable);
        }
        return carService.getAll();
    }

    @Operation(summary = "Добавление машины в гараж")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping
    public CarEntity save(@RequestBody final CarEntity car) {
        return carService.save(car);
    }

    @Operation(summary = "Удаление машины из гаража")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final Long id) {
        if (carService.existsById(id)) {
            carService.deleteById(id);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}
