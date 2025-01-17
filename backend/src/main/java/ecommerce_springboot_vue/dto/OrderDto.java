package ecommerce_springboot_vue.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import ecommerce_springboot_vue.entity.Order;
import jakarta.validation.constraints.NotBlank;
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

  private Order.OrderStatus status;

  private List<OrderItemDto> orderItems;
}
