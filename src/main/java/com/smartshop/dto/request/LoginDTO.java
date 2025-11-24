package com.smartshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Username is required")
    @NotNull(message = "Username cannot be null")
    private String username;


    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password is required")
    private String password;


}
