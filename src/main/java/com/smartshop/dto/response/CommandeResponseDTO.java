package com.smartshop.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeResponseDTO {

    private Long id;
    private Double sousTotal;
    private Double montantRemise;
    private int remise;
    private int TVA;
    private String codePromo;
    private Double totalTTC;
    private Double totalPreTax;
    private String statutCommande;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private int numeroPaiement;
    private Double montantRestant;

    private Long clientId;
    private String clientNom;
    private String clientEmail;

    private List<CommandeItemResponseDTO> commandeItems;
    private List<PaymentResponseDTO> payments;
}
