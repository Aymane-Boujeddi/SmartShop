package com.smartshop.dto.response;

import com.smartshop.enums.StatutPayment;
import com.smartshop.enums.TypePayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long commandeId;
    private String clientName;
    private Double montant;
    private String reference;
    private String banque;
    private int numeroPaiement;
    private TypePayment typePayment;
    private StatutPayment statutPayment;
    private LocalDateTime datePaiment;
    private LocalDateTime dateEncaissement;
}
