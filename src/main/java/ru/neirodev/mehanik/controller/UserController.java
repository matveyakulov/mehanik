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

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление нескольких полей пользователя(обновятся все, которые не null у входящего объекта)")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping("")
    public ResponseEntity<?> update(
            @RequestBody final UserDTO userDTO) {
        try {
            Optional<UserEntity> repUser = userService.getById(userDTO.getId());
            if (repUser.isPresent()) {
                UserEntity userEntity = repUser.get();
                userService.update(userDTO, userEntity);
                return ResponseEntity.accepted().build();
            }
            return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Обновление одного поля пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не существует")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не изменено из-за ошибки")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @PutMapping("/field")
    public ResponseEntity<?> update(@RequestBody final SetFieldRequest request) {
        try {
            userService.setField(request);
            return ResponseEntity.accepted().build();
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Operation(summary = "Получение пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        Optional<UserEntity> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            return ResponseEntity.ok().body(repUser.get());
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Получение данных о текущем пользователе")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            description = "Пользователь",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrent() {
        try {
            return ResponseEntity.ok().body(userService.getCurrentUser());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Получение пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        Optional<UserEntity> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            userService.delete(repUser.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }

    @Operation(summary = "Получение рейтинга пользователя по id")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Double.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @GetMapping("/{id}/rating")
    public ResponseEntity<?> getRatingById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable final Long id) {
        Optional<UserEntity> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            return ResponseEntity.ok().body(userService.getRatingById(id));
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Добавление оценки пользователю")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("/{id}/rating")
    public ResponseEntity<?> addRatingRow(
            @Parameter(description = "Идентификатор пользователя, которому ставится оценка", required = true)
            @PathVariable final Long id,
            @Parameter(description = "Оценка", required = true)
            @RequestParam final Double value) {
        try {
            userService.addRatingRow(id, value);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Получение поставленной оценки пользователю с id от текущего пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK, description = "Если есть - значение оценки, если нет - -1")
    @GetMapping("/{id}/myRating")
    public ResponseEntity<?> getRatingRow(
            @Parameter(description = "Идентификатор пользователя, у которого проверяем поставлена оценка", required = true)
            @PathVariable final Long id) {
        Optional<UserRatingEntity> repUserRating = userService.getRatingRowByUserToId(id);
        if(repUserRating.isEmpty()){
            return ResponseEntity.ok().body(-1);
        } else {
            return ResponseEntity.ok().body(repUserRating.get().getValue());
        }
    }
}
