package com.smartshop.repository;

import com.smartshop.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande , Long> {
    List<Commande> findAllByMontantRestant(Double montantRestant);
}
