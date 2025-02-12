package ecommerce_springboot_vue.dto;

import java.math.BigDecimal;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

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
public class ProductDto {

  private Long id;

  @NotBlank(message = "Name can not be blank")
  @Length(max = 50, message = "Name can not be longer than 50 characters")
  private String name;

  private String description;

  @NotNull(message = "Total stock can not be null")
  @PositiveOrZero(message = "Total stock can not be negative")
  private Integer totalStock;

  @NotNull(message = "Price can not be null")
  @PositiveOrZero(message = "Price can not be negative")
  private BigDecimal price;

  private String imageUrl;

  private Set<CategoryDto> categories;
}