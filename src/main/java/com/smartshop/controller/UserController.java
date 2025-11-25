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
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody AdminCreationDTO adminCreationDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
            return ResponseEntity.ok(userService.createUserAdmin(adminCreationDTO));
    }


    @PostMapping("/client")
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody ClientCreationDTO clientCreationDTO,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(userService.createUserClient(clientCreationDTO));
    }




}
