package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.UserRatingEntity;
import ru.neirodev.mehanik.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final String NOT_FOUND_MESSAGE = "Пользователь с таким id не найден";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление нескольких полей пользователя(обновятся все, которые не null у входящего объекта)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping
    @ResponseStatus(ACCEPTED)
    public void update(@RequestBody final UserDTO userDTO) {
        UserEntity userEntity = userService.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        userService.update(userDTO, userEntity);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление одного поля пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не существует")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не изменено из-за ошибки")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @PutMapping("/field")
    @ResponseStatus(ACCEPTED)
    public void update(@RequestBody final SetFieldRequest request) {
        userService.setField(request);
    }

    @Operation(summary = "Получение пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @GetMapping("/{id}")
    public UserEntity getById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Получение данных о текущем пользователе")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            description = "Пользователь",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @GetMapping("/me")
    public UserEntity getCurrent() {
        return userService.getCurrentUser();
    }

    @Operation(summary = "Получение пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @Operation(summary = "Получение рейтинга пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = NOT_FOUND_MESSAGE)
    @GetMapping("/{id}/rating")
    public void getRatingById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        if (userService.existsById(id)) {
            userService.getRatingById(id);
        } else {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление оценки пользователю")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/rating")
    public void addRatingRow(
            @Parameter(description = "Идентификатор пользователя, которому ставится оценка", required = true)
            @PathVariable final Long id,
            @Parameter(description = "Оценка", required = true)
            @RequestParam final Double value) {
        userService.addRatingRow(id, value);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Получение поставленной оценки пользователю с id от текущего пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK, description = "Если есть - значение оценки, если нет - -1")
    @GetMapping("/{id}/myRating")
    public ResponseEntity<?> getRatingRow(
            @Parameter(description = "Идентификатор пользователя, у которого проверяем поставлена оценка", required = true)
            @PathVariable final Long id) {
        Optional<UserRatingEntity> repUserRating = userService.getRatingRowByUserToId(id);
        if (repUserRating.isEmpty()) {
            return ResponseEntity.ok().body(-1);
        } else {
            return ResponseEntity.ok().body(repUserRating.get().getValue());
        }
    }
}
