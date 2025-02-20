package ecommerce_springboot_vue.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

  private Long id;

  private ProductDto product;

  @NotNull(message = "Quantity can not be null")
  @PositiveOrZero(message = "Quantity can not be negative")
  private Integer quantity;
}
