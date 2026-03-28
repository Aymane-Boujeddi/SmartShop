package com.smartshop.mapper;

import com.smartshop.dto.response.UserResponseDTO;
import com.smartshop.entity.User;
import com.smartshop.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses = {ClientMapper.class})
public interface UserMapper {

    @Mapping(target = "userId",source = "id")
    @Mapping(target = "role",source = "role",qualifiedByName = "roleToString")
    @Mapping(target = "client",ignore = true)
    UserResponseDTO toAdminResponseDto(User user);

    @Mapping(target = "userId",source = "id")
    @Mapping(target = "role",source = "role",qualifiedByName = "roleToString")
    @Mapping(target = "client",source = "client")
    UserResponseDTO toClientResponseDto(User user);

    @Named("roleToString")
    default String roleToString(Role role){
        return role.toString();
    }


}
