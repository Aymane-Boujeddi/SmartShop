package com.smartshop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeRequestDTO {

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Code promo must follow format: PROMO-XXXX")
    private String codePromo;

    @NotNull(message = "Client ID is required")
    @Positive(message = "Client ID must be positive")
    private Long clientId;

    @NotNull(message = "TVA is required")
    private int tva;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<CommandeItemRequestDTO> commandeList;
}
