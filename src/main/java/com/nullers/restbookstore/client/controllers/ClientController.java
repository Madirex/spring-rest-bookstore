package com.nullers.restbookstore.client.controllers;

import com.nullers.restbookstore.NOADD.models.Book;
import com.nullers.restbookstore.client.dto.ClientCreateDto;
import com.nullers.restbookstore.client.dto.ClientDto;
import com.nullers.restbookstore.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.client.models.ErrorResponse;
import com.nullers.restbookstore.client.services.ClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/clients")
public class ClientController {


    private ClientServiceImpl clientService;

    @Autowired
    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }


    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll() {
        return ResponseEntity.ok(clientService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientCreateDto clientDto) {
        return ResponseEntity.ok(clientService.save(clientDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable UUID id,@Valid @RequestBody ClientCreateDto clientDto) {
        return ResponseEntity.ok(clientService.update(id, clientDto));
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<ClientDto> updatePatchBook(@PathVariable UUID id, @RequestParam UUID idBook) {
        ClientDto clientDto = clientService.addBookOfClient(id, idBook);
        //clientDto.getBooks().clear();
        ResponseEntity<ClientDto> client = ResponseEntity.ok(clientDto);
        System.out.println(client);
        return client;
    }

    @PatchMapping("/books/remove/{id}")
    public ResponseEntity<ClientDto> updatePatchBookDelete(@PathVariable UUID id, @PathVariable UUID idBook) {
        return ResponseEntity.ok(clientService.removeBookOfClient(id, idBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*@DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        clientService.deleteAll();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }*/


    @ExceptionHandler(ClientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFunkoNotFound(ClientNotFound exception){
        return new com.nullers.restbookstore.client.models.ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
    @ExceptionHandler(ClientAlreadyExists.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFunkoNotFound(ClientAlreadyExists exception){
        return new com.nullers.restbookstore.client.models.ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
