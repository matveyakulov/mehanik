package ru.neirodev.mehanik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neirodev.mehanik.dto.SetFieldRequest;
import ru.neirodev.mehanik.dto.UserDTO;
import ru.neirodev.mehanik.entity.User;
import ru.neirodev.mehanik.service.UserService;

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

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok().body(userService.save(userDTO));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

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

    @PutMapping("/field")
    public ResponseEntity<?> update(@RequestBody SetFieldRequest request) {
        try {
            userService.setField(request);
            return ResponseEntity.accepted().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            return ResponseEntity.ok().body(repUser.get());
        }
        return new ResponseEntity<>("Пользователь с таким id не найден", NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<User> repUser = userService.getById(id);
        if (repUser.isPresent()) {
            userService.delete(repUser.get());
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity("Пользователь с таким id не найден", NOT_FOUND);
    }
}
