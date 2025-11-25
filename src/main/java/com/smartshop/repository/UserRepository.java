package com.smartshop.repository;

import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findUserByUsername(String username);
    public User findUserByIdAndRole_Client(Long id);
    List<User> findAllByRole(Role role);
}
