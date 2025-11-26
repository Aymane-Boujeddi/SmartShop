package com.smartshop.dto.request;

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

    private String codePromo;

    private Long clientId;

    private int TVA;

    private List<CommandeItemRequestDTO> commandeList;
}
