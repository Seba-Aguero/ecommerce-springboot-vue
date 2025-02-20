package ecommerce_springboot_vue.dto.request.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AddCartItemRequest {

  @NotNull(message = "Product ID is required")
  private Long productId;

  @NotNull(message = "Quantity is required")
  @PositiveOrZero
  private Integer quantity;
}