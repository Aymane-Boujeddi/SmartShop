package com.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitResponseDTO {

    private Long id;
    private String nom;
    private Double prixUnitaire;
    private int stockDisponible;
    private boolean deleted;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

}
