package com.smartshop.service.impl;

import com.smartshop.dto.request.AdminCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import com.smartshop.exception.UsernameDuplicateExcception;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.UserService;
import com.smartshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponseDTO createUserAdmin(AdminCreationDTO adminCreationDTO) {
        String username = adminCreationDTO.getUsername();
        String password = adminCreationDTO.getPassword();

        checkDuplicatedUsername(username);

        User user = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .role(Role.ADMIN)

                .build();
        User savedUser = userRepository.save(user);
        return userMapper.toAdminResponseDto(user);
    }



    private void checkDuplicatedUsername(String username){
        User user = userRepository.findUserByUsername(username);

        if(user != null){
            throw new UsernameDuplicateExcception("This username already exists : " + username);
        }
    }
}
