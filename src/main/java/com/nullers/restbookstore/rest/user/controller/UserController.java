package com.nullers.restbookstore.rest.user.controller;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.model.User;
import com.nullers.restbookstore.rest.user.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador de usuarios
 */
@RestController
@Slf4j
@RequestMapping("/users") // Es la ruta del controlador
public class UserController {
    /**
     * Servicio de usuarios
     */
    private final UserService usersService;

    @Autowired
    public UserController(UserService userService) {
        this.usersService = userService;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username  Nombre de usuario
     * @param email     Email del usuario
     * @param isDeleted Si el usuario esta borrado
     * @param page      Pagina
     * @param size      Tamaño de la pagina
     * @param sortBy    Campo por el que ordenar
     * @param direction Direccion de la ordenacion
     * @return Lista de usuarios
     */
    @GetMapping
    public Page<UserResponse> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));

    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id Id del usuario
     * @return Usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable UUID id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un usuario
     *
     * @param userRequest Usuario a crear
     * @return Usuario creado
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          Id del usuario
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id Id del usuario
     * @return Respuesta vacia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario autenticado
     *
     * @param user Usuario autenticado
     * @return Usuario autenticado
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserInfoResponse> me( User user) {
        log.info("Obteniendo usuario");
        // Esta autenticado, por lo que devolvemos sus datos ya sabemos su id
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }

    /**
     * Actualiza el usuario autenticado
     *
     * @param user        Usuario autenticado
     * @param userRequest Usuario a actualizar
     * @return Usuario actualizado
     */
    @PutMapping("/me/profile")
    public ResponseEntity<UserResponse> updateMe( User user, @Valid @RequestBody UserRequest userRequest) {
        log.info("updateMe: user: {}, userRequest: {}", user, userRequest);
        return ResponseEntity.ok(usersService.update(user.getId(), userRequest));
    }

    /**
     * Borra el usuario autenticado
     *
     * @param user Usuario autenticado
     * @return Respuesta vacia
     */
    @DeleteMapping("/me/profile")
    public ResponseEntity<Void> deleteMe(User user) {
        log.info("deleteMe: user: {}", user);
        usersService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Maneja las excepciones de validacion
     *
     * @param ex Excepcion de validacion
     * @return Mapa de errores de validacion
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
