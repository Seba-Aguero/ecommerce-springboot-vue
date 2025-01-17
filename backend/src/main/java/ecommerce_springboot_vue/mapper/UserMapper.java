package ecommerce_springboot_vue.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.UserDto;
import ecommerce_springboot_vue.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final ModelMapper modelMapper;

  public User dtoToEntity(UserDto dto) {
    return modelMapper.map(dto, User.class);
  }

  public UserDto entityToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }
}
