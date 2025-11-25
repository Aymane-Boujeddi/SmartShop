package com.smartshop.repository;

import com.smartshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findUserByUsername(String username);
    public User findUserByClient_Id(Long id);
}
