package ecommerce_springboot_vue.mapper;

import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

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
    Product product = modelMapper.map(dto, Product.class);

    // Ensure categories is never null
    if (product.getCategories() == null) {
      product.setCategories(new HashSet<>());
    }

    return product;
  }

  public ProductDto entityToDto(Product product) {
    Set<CategoryDto> categories = product.getCategories() != null
      ? product.getCategories().stream()
        .map(categoryMapper::entityToDto)
        .collect(Collectors.toSet())
      : new HashSet<>();

    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    productDto.setCategories(categories);
    return productDto;
  }
}
