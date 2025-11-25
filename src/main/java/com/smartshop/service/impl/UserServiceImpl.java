package com.smartshop.service.impl;

import com.smartshop.dto.request.AdminCreationDTO;
import com.smartshop.dto.request.ClientCreationDTO;
import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.Client;
import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import com.smartshop.exception.DuplicateCredentialsExcception;
import com.smartshop.exception.UserNotFoundException;
import com.smartshop.mapper.UserMapper;
import com.smartshop.repository.ClientRepository;
import com.smartshop.repository.UserRepository;
import com.smartshop.service.UserService;
import com.smartshop.util.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
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

        return userMapper.toClientResponseDto(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toClientResponseDto)
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

        User user = userRepository.findUserByClient_Id(id);
        if(user == null) {
            throw new UserNotFoundException("Client not found with this Id : " + id);
        }
        return userMapper.toClientResponseDto(user);
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
}
