package com.smartshop.repository;

import com.smartshop.entity.Client;
import com.smartshop.entity.Commande;
import com.smartshop.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande , Long> {
    List<Commande> findAllByMontantRestant(Double montantRestant);

    List<Commande> findAllByMontantRestantAndStatutCommande(Double montantRestant, StatutCommande statutCommande);



    boolean existsByCodePromoAndClient(String codePromo, Client client);

}
