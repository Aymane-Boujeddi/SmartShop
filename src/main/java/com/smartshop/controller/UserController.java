package com.smartshop.controller;

import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.service.ClientService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ClientService clientService;


    @PostMapping("/client")
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody ClientCreationDTO clientCreationDTO,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(clientService.createUserClient(clientCreationDTO));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(clientService.getAllUsers());
    }

    @GetMapping("/clients")
    public ResponseEntity<List<UserResponseDTO>> getAllClients(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<UserResponseDTO> getClientById(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Map<String , Object>> deleteClientById(@PathVariable Long id, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(clientService.deleteClientById(id));
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<UserResponseDTO> updateClientById(@PathVariable Long id, @Valid @RequestBody ClientUpdateDTO clientUpdateDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return  ResponseEntity.ok(clientService.updateClientById(id,clientUpdateDTO));
    }
}
