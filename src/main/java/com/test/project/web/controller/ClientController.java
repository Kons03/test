package com.test.project.web.controller;

import com.test.project.dto.ClientDto;
import com.test.project.entity.Client;
import com.test.project.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@Tag(name = "Клиентские операции")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/getbyphone/{phone}")
    @Operation(summary = "Поиск по телефону")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Client> getClientByPhone(@PathVariable String phone) {
        return ResponseEntity.ok().body(clientService.getClientByPhone(phone));
    }

    @GetMapping("/getbyname")
    @Operation(summary = "Поиск по имени")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Page<Client>> getClientByName(@RequestParam int page, @RequestParam int size, @RequestParam String name) {
        return ResponseEntity.ok().body(clientService.getClientsByName(page, size, name));
    }

    @GetMapping("/getbymail/{mail}")
    @Operation(summary = "Поиск по почтовому адресу ")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Client> getClientByMail(@PathVariable String mail) {
        return ResponseEntity.ok().body(clientService.getClientByMail(mail));
    }

    @GetMapping("/getbybirthdate")
    @Operation(summary = "Поиск по дате рождения")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Page<Client>> getClientByBirthdate(@RequestParam int page, @RequestParam int size, @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthdate) {
        return ResponseEntity.ok().body(clientService.getClientsByBirthdate(page, size, birthdate));
    }


    @PostMapping("/registerclient")
    @Operation(summary = "Регистрация нового клиента")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Client> registerClient(@RequestBody @Valid ClientDto clientDto) {
        return new ResponseEntity<>(clientService.registerClient(clientDto), HttpStatus.CREATED);
    }

    @PutMapping("/moneytransfer")
    @Operation(summary = "Трансфер средств между клиентами")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Boolean> moneyTransfer(@RequestParam long sourceId, @RequestParam long recipientId, @RequestParam float amount) {
        return ResponseEntity.ok(clientService.moneyTransfer(sourceId, recipientId, amount));
    }
}