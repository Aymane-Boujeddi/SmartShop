package com.smartshop.repository;

import com.smartshop.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande , Long> {
}
