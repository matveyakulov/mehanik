package ru.neirodev.mehanik.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.entity.security.Session;
import ru.neirodev.mehanik.security.JwtFilter;
import ru.neirodev.mehanik.security.JwtTokenUtil;
import ru.neirodev.mehanik.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Авторизация пользователя", operationId = "auth", description = "Метод для получения нового access token'а.\n\n"
            + "Для ресурсов требующих авторизации, access token должен передаваться в заголовке "
            + HttpHeaders.AUTHORIZATION + " со схемой установления подлинности Bearer, " + "либо в заголовке "
            + HttpHeaders.COOKIE + " с именем " + JwtFilter.COOKIE_NAME + ". "
            + "Если в оба этих заголовка установлены значения, то " + HttpHeaders.AUTHORIZATION
            + " имеет приоритет.\n\n"
            + "**ВАЖНО:** в Swagger UI кука с токеном не отображается, так как у неё установлен флаг HttpOnly, но тем не менее она корректно запоминается браузером и участвует в последующих запросах.")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_OK, description = "В случае успешного выполнения запроса, сервер вернёт ответ с кодом "
            + HttpServletResponse.SC_OK + ", cookie с именем " + JwtFilter.COOKIE_NAME
            + " в заголовке и сессию пользователя в теле ответа. Access токен имеет срок годности(в минутах) "
            + JwtTokenUtil.ACCESS_TOKEN_VALIDITY_MINUTES
            + " минут. Refresh токен имеет неограниченный срок действия.", content = @Content(mediaType = "application/json"), headers = @Header(name = HttpHeaders.SET_COOKIE, description = JwtFilter.COOKIE_NAME
            + ": access token пользователя", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_FORBIDDEN, description = "Регистрация не подтверждена")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_NOT_FOUND, description = "Пользователь с такими учетными данными не найден")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam final String login, @RequestParam final String password) {
        if ((login != null) && !login.isEmpty() && (password != null) && !password.isEmpty())
            return ResponseEntity.ok().body(authService.login(login, password));
        else
            return new ResponseEntity<>("Неверные данные", NOT_FOUND);
    }

    @Operation(summary = "Обновление access token", operationId = "refresh", description = "Метод получения нового access token для указанного refresh token")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_OK, description = "В случае успешного выполнения запроса, сервер вернёт обновлённую сессию пользователя", content = @Content(mediaType = "application/json"), headers = @Header(name = HttpHeaders.SET_COOKIE, description = JwtFilter.COOKIE_NAME
            + ": access token пользователя", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "" + HttpServletResponse.SC_BAD_REQUEST, description = "Refresh token не задан")
    @ApiResponse(responseCode = ""
            + HttpServletResponse.SC_NOT_FOUND, description = "Сессия с данным refresh token'ом не найдена")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam final String refreshToken, final HttpServletRequest request,
                                          final HttpServletResponse response) {
        try {
            if ((refreshToken != null) && !refreshToken.isEmpty()) {
                Session session = authService.refreshToken(refreshToken, request, response);
                if (session != null) {
                    return ResponseEntity.ok().build();
                } else {
                    return new ResponseEntity<>("Сессия с данным refresh token'ом не найдена", NOT_FOUND);
                }
            } else {
                return ResponseEntity.badRequest().body("Неверный токен");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Создание завершилось с ошибкой: " + e.getMessage());
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
    public ResponseEntity<?> logout(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            authService.logout(request, response);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Создание завершилось с ошибкой: " + e.getMessage());
        }
    }
}
