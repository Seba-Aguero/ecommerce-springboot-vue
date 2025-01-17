package ecommerce_springboot_vue.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.CartItemDto;
import ecommerce_springboot_vue.entity.CartItem;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

  private final ModelMapper modelMapper;

  public CartItem dtoToEntity(CartItemDto dto) {
    return modelMapper.map(dto, CartItem.class);
  }

  public CartItemDto entityToDto(CartItem cartItem) {
    return modelMapper.map(cartItem, CartItemDto.class);
  }

}
