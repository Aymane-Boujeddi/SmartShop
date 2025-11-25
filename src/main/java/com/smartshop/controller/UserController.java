package com.smartshop.controller;

import com.smartshop.dto.request.AdminCreationDTO;
import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.service.UserService;
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

    private final UserService userService;

    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody AdminCreationDTO adminCreationDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
            return ResponseEntity.ok(userService.createUserAdmin(adminCreationDTO));
    }


    @PostMapping("/client")
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody ClientCreationDTO clientCreationDTO,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.createUserClient(clientCreationDTO));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/clients")
    public ResponseEntity<List<UserResponseDTO>> getAllClients(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.getAllClients());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<UserResponseDTO> getClientById(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.getClientById(id));
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Map<String , Object>> deleteClientById(@PathVariable Long id, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.deleteClientById(id));
    }
}
