package ecommerce_springboot_vue.dto;

import java.math.BigDecimal;
// import java.util.List;

import org.hibernate.validator.constraints.Length;

import ecommerce_springboot_vue.entity.Order;
import jakarta.validation.constraints.NotBlank;
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
public class OrderDto {

  private Long id;

  private Long userId;

  @NotBlank(message = "Address can not be blank")
  @Length(max = 50, message = "Address can not be longer than 50 characters")
  private String address;

  @NotBlank(message = "Phone can not be blank")
  @Length(max = 15, message = "Phone can not be longer than 15 characters")
  private String phone;

  @NotNull(message = "Total amount can not be null")
  @PositiveOrZero(message = "Total amount can not be negative")
  private BigDecimal totalAmount;

  private Order.OrderStatus status;

  // private List<OrderItemDto> orderItems;
}
