package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.DTO.UserAccountDTO;
import com.maycollins.LlantasApi.DTO.UserAccountResponseDTO;
import com.maycollins.LlantasApi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<UserAccountResponseDTO> createUser(@RequestBody UserAccountDTO userDTO) {
        return ResponseEntity.ok(userAccountService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userAccountService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserAccountResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userAccountService.getAllUsers());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponseDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody UserAccountDTO userDTO) {
        return ResponseEntity.ok(userAccountService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userAccountService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}