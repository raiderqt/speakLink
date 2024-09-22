package com.example.SpeakLink.Mapper;


import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "firstName", source = "name", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "name", qualifiedByName = "extractLastName")
    UserDto toUserDTO(User user);

    @Mapping(target = "name", expression = "java(userDto.getFirstName() + ' ' + userDto.getLastName())")
    User toUser(UserDto userDto);

    //извлечения имени
    @Named("extractFirstName")
    default String extractFirstName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] parts = name.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    //извлечения фамилии
    @Named("extractLastName")
    default String extractLastName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] parts = name.split(" ");
        return parts.length > 1 ? parts[1] : "";
    }
}
