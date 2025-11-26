package com.smartshop.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandeItemResponseDTO {

    private Long id;
    private int quantite;
    private Double prixUnitaire;
    private Double totalLigne;

    private Long produitId;
    private String produitNom;
}
