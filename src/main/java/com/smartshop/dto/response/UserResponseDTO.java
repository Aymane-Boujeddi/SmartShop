package com.smartshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long userId;

    private String username;

    private String role;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientResponseDTO client;
}
