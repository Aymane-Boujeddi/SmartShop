package com.smartshop.mapper;

import com.smartshop.dto.response.ClientResponseDTO;
import com.smartshop.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "clientId",source = "id")
    ClientResponseDTO toClientResponseDto(Client client);
}
