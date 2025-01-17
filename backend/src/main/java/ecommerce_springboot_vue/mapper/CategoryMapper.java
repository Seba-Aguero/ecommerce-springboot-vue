package ecommerce_springboot_vue.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ecommerce_springboot_vue.dto.CategoryDto;
import ecommerce_springboot_vue.entity.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

  private final ModelMapper modelMapper;

  public Category dtoToEntity(CategoryDto dto) {
    return modelMapper.map(dto, Category.class);
  }

  public CategoryDto entityToDto(Category category) {
    return modelMapper.map(category, CategoryDto.class);
  }
}
