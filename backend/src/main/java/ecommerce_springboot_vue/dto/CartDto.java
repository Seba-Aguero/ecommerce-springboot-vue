package ecommerce_springboot_vue.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

  private Long id;

  private Long userId;

  private List<CartItemDto> cartItems;
}
