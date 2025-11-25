package com.smartshop.mapper;

import com.smartshop.dto.response.ClientResponseDTO;
import com.smartshop.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {


    ClientResponseDTO toClientResponseDto(Client client);
}
