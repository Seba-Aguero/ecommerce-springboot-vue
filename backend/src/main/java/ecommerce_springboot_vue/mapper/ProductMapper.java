package ecommerce_springboot_vue.mapper;

import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.CategoryDto;
import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.entity.Product;

@Component
@RequiredArgsConstructor
public class ProductMapper {

  private final ModelMapper modelMapper;
  private final CategoryMapper categoryMapper;

  public Product dtoToEntity(ProductDto dto) {
    return modelMapper.map(dto, Product.class);
  }

  public ProductDto entityToDto(Product product) {
    Set<CategoryDto> categories = product.getCategories().stream()
      .map(categoryMapper::entityToDto)
      .collect(Collectors.toSet());

    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    productDto.setCategories(categories);
    return productDto;
  }
}
