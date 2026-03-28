package com.smartshop.service.impl;

import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import com.smartshop.exception.DuplicateCredentialsExcception;
import com.smartshop.exception.UserNotFoundException;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.ClientService;
import com.smartshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;



    // Use Mapper to Entity instead of manual mapping

    @Override
    @Transactional
    public UserResponseDTO createUserClient(ClientCreationDTO clientCreationDTO) {
        String username = clientCreationDTO.getUsername();
        String password = clientCreationDTO.getPassword();

        checkDuplicatedUsername(username);
        checkDuplicateEmail(clientCreationDTO.getEmail());


        Client client = Client.builder()
                .nom(clientCreationDTO.getNom())
                .email(clientCreationDTO.getEmail())
                .build();

        User user = User.builder()
                .username(username)
                .password(PasswordUtil.hashPassword(password))
                .role(Role.CLIENT)
                .client(client)
                .build();
        client.setUser(user);
        User savedUser = userRepository.save(user);

        return userMapper.toClientResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    if(user.getRole().equals(Role.ADMIN)){
                        return userMapper.toAdminResponseDto(user);
                    }else{
                        return userMapper.toClientResponseDto(user);
                    }
                })
                .toList();
    }

    @Override
    public List<UserResponseDTO> getAllClients() {
        List<User> clients = userRepository.findAllByRole(Role.CLIENT);
        return clients.stream()
                .map(userMapper::toClientResponseDto)
                .toList();
    }

    @Override
    public UserResponseDTO getClientById(Long id) {

        User user = getUserById(id);

        return userMapper.toClientResponseDto(user);
    }

    @Override
    public Map<String, Object> deleteClientById(Long id) {
        User user = getUserById(id);

        Map<String , Object> response = new HashMap<>();

        userRepository.delete(user);
        response.put("message","Client deleted successfully");
        response.put("Id", id.toString());

        return response;
    }

    @Override
    public UserResponseDTO updateClientById(Long id, ClientUpdateDTO clientUpdateDTO) {
        User user = getUserById(id);
        if(!user.getClient().getEmail().equals(clientUpdateDTO.getEmail())){
            checkDuplicateEmail(clientUpdateDTO.getEmail());
        }
        user.getClient().setNom(clientUpdateDTO.getNom());
        user.getClient().setEmail(clientUpdateDTO.getEmail());

        User savedUser = userRepository.save(user);
        return userMapper.toClientResponseDto(savedUser);
    }

    private void checkDuplicatedUsername(String username){
        User user = userRepository.findUserByUsername(username);

        if(user != null){
            throw new DuplicateCredentialsExcception("This username already exists : " + username);
        }
    }
    private void checkDuplicateEmail(String email){
        Client client = clientRepository.findClientByEmail(email);

        if(client != null){
            throw new DuplicateCredentialsExcception("This email already exists : " + email);
        }
    }
    private User getUserById(Long id){
        User user = userRepository.findUserByIdAndRole(id,Role.CLIENT);
        if(user == null) {
            throw new UserNotFoundException("Client not found with this Id : " + id);
        }
        return user;
    }
}
