package ecommerce_springboot_vue.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.CartItemDto;
import ecommerce_springboot_vue.entity.Cart;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartMapper {

  private final ModelMapper modelMapper;
  private final CartItemMapper cartItemMapper;

  public Cart dtoToEntity(CartDto dto) {
    return modelMapper.map(dto, Cart.class);
  }

  public CartDto entityToDto(Cart cart) {
    List<CartItemDto> cartItems = cart.getCartItems().stream()
      .map(cartItemMapper::entityToDto)
      .collect(Collectors.toList());

    CartDto cartDto = modelMapper.map(cart, CartDto.class);
    cartDto.setCartItems(cartItems);
    return cartDto;
  }

}
