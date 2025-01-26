package ecommerce_springboot_vue.dto;

import ecommerce_springboot_vue.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  private User.Role role;
}
