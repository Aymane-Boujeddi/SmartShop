package com.smartshop.controller;


import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.AuthResponseDTO;
import com.smartshop.service.AuthService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> handleLogin(@Valid @RequestBody LoginDTO loginDTO, HttpSession session){
        return ResponseEntity.ok(authService.handleLogin(loginDTO,session));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> handleLogout(HttpSession session){
        SecurityUtil.checkAuthentication(session);
        return ResponseEntity.ok(authService.handleLogout(session));
    }
}
