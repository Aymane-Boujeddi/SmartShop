package com.smartshop.service;

import com.smartshop.dto.request.AdminCreationDTO;
import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;

public interface UserService {
    public UserResponseDTO createUserAdmin(AdminCreationDTO adminCreationDTO);
    public UserResponseDTO createUserClient(ClientCreationDTO clientCreationDTO);
}
