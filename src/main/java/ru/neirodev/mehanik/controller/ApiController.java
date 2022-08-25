package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neirodev.mehanik.api.model.Car;
import ru.neirodev.mehanik.api.model.CarPart;
import ru.neirodev.mehanik.api.model.Make;
import ru.neirodev.mehanik.api.model.Model;
import ru.neirodev.mehanik.enums.CarType;
import ru.neirodev.mehanik.service.ApiService;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @Operation(description = "Список транспортных средств")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @GetMapping("/carTypes")
    public CarType[] carTypes() {
        return CarType.class.getEnumConstants();
    }

    @Operation(summary = "Список марок машин")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Make.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @GetMapping("/makes")
    public ResponseEntity<?> getMakes(
            @Parameter(description = "Тип транспортного средства")
            @RequestParam final CarType carType) {
        try {
            return ResponseEntity.ok().body(apiService.getMakesRequest(carType.getName()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Список моделей машин")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @GetMapping("/models")
    public ResponseEntity<?> getModels(
            @Parameter(description = "Тип транспортного средства")
            @RequestParam final CarType carType,
            @Parameter(description = "Марка машины")
            @RequestParam final Long make) {
        try {
            return ResponseEntity.ok().body(apiService.getModelsRequest(make, carType.getName()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Список модификаций модели")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @GetMapping("/modifications")
    public ResponseEntity<?> getModifications(
            @Parameter(description = "Тип транспортного средства")
            @RequestParam final CarType carType,
            @Parameter(description = "Марка машины(makeId из /makes)")
            @RequestParam final Long make,
            @Parameter(description = "Модель машины(modelId из /models")
            @RequestParam final Long model) {
        try {
            return ResponseEntity.ok().body(apiService.getCarsRequest(make, model, carType.getName()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @Operation(summary = "Список запчастей к модели")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @GetMapping("/carParts")
    public List<CarPart> carPartsList(
            @Parameter(description = "Тип транспортного средства")
            @RequestParam final CarType carType,
            @Parameter(description = "Модификация машины(carId из /modifications")
            @RequestParam final Long kid) {
        try {
            return apiService.carPartsList(carType.getCode(), String.valueOf(kid));
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
