package com.smartshop.controller;

import com.smartshop.dto.request.AdminCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/admin")
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody AdminCreationDTO adminCreationDTO){
            return ResponseEntity.ok(userService.createUserAdmin(adminCreationDTO));
    }



}
