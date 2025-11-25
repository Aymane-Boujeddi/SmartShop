package com.smartshop.service;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.AuthResponseDTO;
import com.smartshop.dto.response.UserResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    public AuthResponseDTO handleLogin(LoginDTO loginDTO, HttpSession session);
    public AuthResponseDTO handleLogout(HttpSession session);
    public UserResponseDTO getCurrentUser(HttpSession session);
}
