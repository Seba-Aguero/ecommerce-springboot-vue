package ecommerce_springboot_vue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce_springboot_vue.dto.CategoryDto;
import ecommerce_springboot_vue.mapper.CategoryMapper;
import ecommerce_springboot_vue.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

  private final ICategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public List<CategoryDto> getAllCategories() {
    return categoryRepository.findAll().stream()
      .map(categoryMapper::entityToDto)
      .toList();
  }
}