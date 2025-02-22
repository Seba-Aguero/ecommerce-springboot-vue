package ecommerce_springboot_vue.mapper;

// import java.util.List;
// import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.entity.Order;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

  private final ModelMapper modelMapper;

  public Order dtoToEntity(OrderDto dto) {
    return modelMapper.map(dto, Order.class);
  }

  public OrderDto entityToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }

}
