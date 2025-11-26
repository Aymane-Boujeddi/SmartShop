package com.smartshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitRequestDTO {



    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String nom;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be in format: XXXXXXXXXX.XX (maximum 10 digits before decimal, 2 after)")
    private Double prixUnitaire;

    @Min(value = 0, message = "Available stock cannot be negative")
    @Max(value = 999999, message = "Available stock cannot exceed 999999")
    private int stockDisponible;


}
