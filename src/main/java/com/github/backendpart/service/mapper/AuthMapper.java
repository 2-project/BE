package com.github.backendpart.service.mapper;

import com.github.backendpart.web.dto.users.RequestUserDto;
import com.github.backendpart.web.dto.users.UserDto;
import com.github.backendpart.web.entity.users.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// componentModel = spring으로 지정하게 되면 Mapper클래스가 Bean으로 등록
@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    UserDto userToUserDto(UserEntity userEntity);

    @Mapping(target = "userPwd", ignore = true)
    UserEntity userDtoToUser(UserDto userDto);

    UserEntity requestUserDtoToUser(RequestUserDto requestUserDto);

    UserDto requestUserDtoToUserDto(RequestUserDto requestUserDto);
}
