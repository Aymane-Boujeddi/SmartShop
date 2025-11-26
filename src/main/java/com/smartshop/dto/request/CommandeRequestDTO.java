package com.smartshop.dto.request;

import java.util.List;

public class CommandeRequestDTO {

    private String codePromo;

    private Long clientId;
     
    private int TVA;

    private List<CommandeItemRequestDTO> commandeList;
}
