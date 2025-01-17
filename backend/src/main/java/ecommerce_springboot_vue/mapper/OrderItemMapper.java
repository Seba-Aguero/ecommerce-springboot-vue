package ecommerce_springboot_vue.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.OrderItemDto;
import ecommerce_springboot_vue.entity.OrderItem;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

  private final ModelMapper modelMapper;

  public OrderItem dtoToEntity(OrderItemDto dto) {
    return modelMapper.map(dto, OrderItem.class);
  }

  public OrderItemDto entityToDto(OrderItem orderItem) {
    return modelMapper.map(orderItem, OrderItemDto.class);
  }

}
