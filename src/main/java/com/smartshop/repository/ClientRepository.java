package com.smartshop.repository;

import com.smartshop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client ,Long> {
    public Client findClientByEmail(String email);
}
