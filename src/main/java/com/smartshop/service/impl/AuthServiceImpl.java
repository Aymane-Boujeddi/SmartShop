package com.smartshop.service.impl;

import com.smartshop.dto.request.LoginDTO;
import com.smartshop.dto.response.AuthResponseDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import com.smartshop.exception.AlreadyLoggedInException;
import com.smartshop.exception.InvalidCredentialsException;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.AuthService;
import com.smartshop.util.PasswordUtil;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthResponseDTO handleLogin(LoginDTO loginDTO, HttpSession session) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        if (session.getAttribute("username") != null) {
            throw new AlreadyLoggedInException("User is already logged in . ");
        }

        User user = userRepository.findUserByUsername(username);
        if(user == null || !PasswordUtil.checkPassword(password,user.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password");
        }
        session.setAttribute("username",user.getUsername());
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());



        return AuthResponseDTO.builder()
                .message("Login successful")
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build();
    }

    @Override
    public AuthResponseDTO handleLogout(HttpSession session) {
        String username = SecurityUtil.getCurrentUsername(session);
        String role = SecurityUtil.getCurrentUserRole(session).toString();
        session.invalidate();
        return AuthResponseDTO.builder()
                .message("Logout successful")
                .username(username)
                .role(role)
                .build();
    }

    @Override
    public UserResponseDTO getCurrentUser(HttpSession session) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId(session)).get();

        if(user.getRole().equals(Role.ADMIN)){
            return userMapper.toAdminResponseDto(user);
        }else{
            return userMapper.toClientResponseDto(user);
        }

    }


}
