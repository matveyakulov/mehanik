package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.SmsDTO;
import ru.neirodev.mehanik.entity.UserEntity;
import ru.neirodev.mehanik.entity.security.Session;
import ru.neirodev.mehanik.security.JwtFilter;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.AuthService;
import ru.neirodev.mehanik.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Отправка смс пользователю", description = "Метод для отправки смс пользователю")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_OK, description = "Смс успешно отправлена")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NOT_FOUND, description = "Неверные данные")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Parameter(name = "Номер телефона")
            @RequestParam final String phone,
            @Parameter(name = "Секретный ключ")
            @RequestParam final String secret) {
        if (authService.sendSms(new SmsDTO(phone, secret))) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>("Неверные данные", NOT_FOUND);
        }
    }

    @Operation(summary = "Подтверждение кода из смс", description = "Метод для получения подтверждения кода из смс")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_OK, description = "В случае успешного выполнения запроса, сервер вернёт ответ с кодом "
            + HttpServletResponse.SC_OK + ", cookie с именем " + JwtFilter.COOKIE_NAME
            + " в заголовке и сессию пользователя в теле ответа. Access токен имеет срок годности(в минутах) "
            + JwtTokenUtil.ACCESS_TOKEN_VALIDITY_MINUTES
            + " минут. Refresh токен имеет неограниченный срок действия.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Session.class)),
            headers = @Header(name = HttpHeaders.SET_COOKIE, description = JwtFilter.COOKIE_NAME
                    + ": access token пользователя", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_BAD_REQUEST, description = "Неверный код")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с таким номером не существует")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(
            @Parameter(name = "Номер телефона")
            @RequestParam final String phone,
            @Parameter(name = "Код подтверждения")
            @RequestParam final String code,
            final HttpServletRequest request, final HttpServletResponse response) {
        UserEntity user = userService.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким номером не существует"));
        if (user.getSmscode().equals(code)) {
            return ResponseEntity.ok().body(authService.startSession(user, request, response));
        } else {
            return ResponseEntity.badRequest().body("Неверный код");
        }
    }

    @Operation(summary = "Обновление access token", operationId = "refresh", description = "Метод получения нового access token для указанного refresh token")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_OK, description = "В случае успешного выполнения запроса, сервер вернёт обновлённую сессию пользователя", content = @Content(mediaType = "application/json"), headers = @Header(name = HttpHeaders.SET_COOKIE, description = JwtFilter.COOKIE_NAME
            + ": access token пользователя", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_BAD_REQUEST, description = "Refresh token не задан")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_NOT_FOUND, description = "Сессия с данным refresh token'ом не найдена")
    @PostMapping("/refresh")
    public void refreshToken(
            @Parameter(name = "Access токен")
            @RequestParam final String refreshToken,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        Session session = authService.refreshToken(refreshToken, request, response);
        if (session == null) {
            throw new EntityNotFoundException("Сессия с данным refresh token'ом не найдена");
        }
    }

    @Operation(summary = "Закрытие пользовательской сессии",
            operationId = "logout",
            description = "Завершение текущей пользовательской сессии. После выполнения данного запроса текущий api_token удаляется из БД и все последующие запросы с этим токеном будут возвращать код ошибки "
                    + HttpServletResponse.SC_FORBIDDEN + " и сообщение 'Invalid token'")
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_NO_CONTENT, description = "В случае успешного выполнения запроса, сервер вернёт просроченный cookie с именем "
            + JwtFilter.COOKIE_NAME + " и с пустым значением.",
            headers = @Header(name = HttpHeaders.SET_COOKIE, description = JwtFilter.COOKIE_NAME + ": пустая просроченная кука для очистки значения на клиенте", schema = @Schema(type = "string")))
    @DeleteMapping("/logout")
    public void logout(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        authService.logout(request, response);
    }
}
