package com.smartshop.repository;

import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findUserByUsername(String username);
    public User findUserByClient_Id(Long id);

    List<User> findAllByRole(Role role);
}
