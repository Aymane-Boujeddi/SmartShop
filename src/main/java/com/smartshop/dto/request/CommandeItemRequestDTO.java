package com.smartshop.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeItemRequestDTO {


    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantite;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long produitId;
}
