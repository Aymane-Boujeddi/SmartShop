package com.smartshop.service;

import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;

import java.util.List;
import java.util.Map;

public interface ClientService {
    public UserResponseDTO createUserClient(ClientCreationDTO clientCreationDTO);
    public List<UserResponseDTO> getAllClients();
    public List<UserResponseDTO> getAllUsers();
    public UserResponseDTO getClientById(Long id);
    public Map<String , Object> deleteClientById(Long id);
}
