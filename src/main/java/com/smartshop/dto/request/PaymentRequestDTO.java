package com.smartshop.dto.request;

import com.smartshop.enums.TypePayment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "Commande ID is required")
    private Long commandeId;

    @NotBlank(message = "Reference is required")
    private String reference;

    @NotNull(message = "Montant is required")
    @Min(value = 1, message = "Montant must be greater than 0")
    private Double montant;

    @NotBlank(message = "Banque is required")
    private String banque;

    @NotNull(message = "Type paiement is required")
    private TypePayment typePayment;



}
