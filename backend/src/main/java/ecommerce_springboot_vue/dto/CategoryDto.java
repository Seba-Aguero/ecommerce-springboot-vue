package ecommerce_springboot_vue.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  private Long id;

  @NotBlank(message = "Name cannot be blank")
  @Length(max = 50, message = "Name cannot be longer than 50 characters")
  private String name;
}
