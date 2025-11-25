package com.smartshop.service.impl;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.LoginResponseDTO;
import com.smartshop.entity.User;
import com.smartshop.exception.InvalidCredentialsException;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AuthService;
import com.smartshop.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    @Override
    public LoginResponseDTO handleLogin(LoginDTO loginDTO,HttpSession session) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = userRepository.findUserByUsername(username);
        if(user == null || !PasswordUtil.checkPassword(password,user.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password");
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());



        return LoginResponseDTO.builder()
                .message("Login successful")
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build();
    }


}
