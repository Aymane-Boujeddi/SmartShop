package com.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {

    private Long clientId;
    private String nom;
    private String email;
    private String niveauFidelite;
    private Double montantCumule;
    private Integer totalCommandes;
    private LocalDateTime dateCreation;
    private LocalDateTime datePremiereCommande;
    private LocalDateTime dateDerniereCommande;




}
