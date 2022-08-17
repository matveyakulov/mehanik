package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${v1Prefix}/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создание пользователя",
            description = "Создать пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            description = "Созданный пользователь",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok().body(userService.save(userDTO));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Обновление нескольких полей пользователя(обновятся все, которые не null у входящего обьекта)",
            description = "Обновить пользователя ")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        try {
            Optional<User> repUser = userService.getById(userDTO.getId());
            if (repUser.isPresent()) {
                User user = repUser.get();
                userService.update(userDTO, user);
                return ResponseEntity.accepted().build();
            }
            return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Обновление одного поля пользователя",
            description = "Обновить пользователя ")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_ACCEPTED)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не существует")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR, description = "Поле не изменено из-за ошибки")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @PutMapping("/field")
    public ResponseEntity<?> update(@RequestBody SetFieldRequest request) {
        try {
            userService.setField(request);
            return ResponseEntity.accepted().build();
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Operation(summary = "Получение пользователя по id",
            description = "Получить пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK,
            description = "Пользователь",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            return ResponseEntity.ok().body(repUser.get());
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }

    @Operation(summary = "Получение пользователя по id",
            description = "Получить пользователя")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK)
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким id не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<User> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            userService.delete(repUser.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }
}
