package com.smartshop.service;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.LoginResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    public LoginResponseDTO handleLogin(LoginDTO loginDTO, HttpSession session);
}
