package com.smartshop.service;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.AuthResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    public AuthResponseDTO handleLogin(LoginDTO loginDTO, HttpSession session);
}
