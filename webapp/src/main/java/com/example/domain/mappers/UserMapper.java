package com.example.domain.mappers;

import com.example.domain.UserEntity;
import com.example.domain.UserRoleEntity;
import com.example.model.UserDTO;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface UserMapper {
    @Mapping(target = "roles", source = "userEntity.roles",qualifiedByName = "mapRoles")
    UserDTO map(UserEntity userEntity);

    @Named("mapRoles")
    default List<String> mapRoles(Collection<UserRoleEntity> roles) {
        return roles.stream().map(UserRoleEntity::getName).collect(Collectors.toList());
    }
}

