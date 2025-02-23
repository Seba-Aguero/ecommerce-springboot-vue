package ecommerce_springboot_vue.dto;

import java.math.BigDecimal;

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
public class OrderItemDto {

  private Long id;

  @NotNull(message = "Order ID can not be null")
  private Long orderId;

  @NotNull(message = "Product can not be null")
  private ProductDto product;

  @NotNull(message = "Quantity can not be null")
  @PositiveOrZero(message = "Quantity can not be negative")
  private Integer quantity;

  @NotNull(message = "Price can not be null")
  @PositiveOrZero(message = "Price can not be negative")
  private BigDecimal price;
}
