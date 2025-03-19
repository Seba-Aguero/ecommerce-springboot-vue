package ecommerce_springboot_vue.service;

import ecommerce_springboot_vue.config.FileStorageConfig;
import ecommerce_springboot_vue.dto.CategoryDto;
import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.entity.Category;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.ProductMapper;
import ecommerce_springboot_vue.repository.ICategoryRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final IProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ICategoryRepository categoryRepository;

  private final FileStorageConfig fileStorageConfig;

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(Paths.get(fileStorageConfig.getDirectory()));
    } catch (IOException e) {
      throw new RuntimeException("No se pudo crear el directorio de imágenes");
    }
  }

  public ProductDto createProduct(ProductDto productDto, MultipartFile image) throws IOException{
    Product product = productMapper.dtoToEntity(productDto);

    if(image != null && !image.isEmpty()){
      String fileName = saveImage(image);
      product.setImageUrl(fileName);
    }

    Product savedProduct = productRepository.save(product);
    return productMapper.entityToDto(savedProduct);
  }

  public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile image) throws IOException{
    Product existingProduct = productRepository.findById(id)
      .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

    if (productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Price can not be negative");
    }
    if (productDto.getTotalStock() != null && productDto.getTotalStock() < 0) {
      throw new IllegalArgumentException("Total stock can not be negative");
    }

    existingProduct.setName(productDto.getName());
    existingProduct.setDescription(productDto.getDescription());
    existingProduct.setPrice(productDto.getPrice());
    existingProduct.setTotalStock(productDto.getTotalStock());

    if (productDto.getCategories() != null && !productDto.getCategories().isEmpty()) {
      Set<Long> categoryIds = productDto.getCategories().stream()
        .map(CategoryDto::getId)
        .collect(Collectors.toSet());
      Set<Category> categories = categoryRepository.findAllById(categoryIds)
        .stream()
        .collect(Collectors.toSet());
      existingProduct.setCategories(categories);
    }

    if(image != null && !image.isEmpty()){
      String fileName = saveImage(image);
      existingProduct.setImageUrl(fileName);
    }

    Product updatedProduct = productRepository.save(existingProduct);
    return productMapper.entityToDto(updatedProduct);
  }

  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    // Delete the image file if it exists
    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
      deleteImageFile(product.getImageUrl());
    }

    productRepository.deleteById(id);
  }

  public Page<ProductDto> getAllProducts(
    List<Long> categories,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    String search,
    Pageable pageable)
  {
    Specification<Product> spec = Specification.where(null);

    if (categories != null && !categories.isEmpty()) {
      spec = spec.and((root, query, cb) ->
        root.join("categories").get("id").in(categories));
    }

    if (minPrice != null) {
      spec = spec.and((root, query, cb) ->
        cb.greaterThanOrEqualTo(root.get("price"), minPrice));
    }

    if (maxPrice != null) {
      spec = spec.and((root, query, cb) ->
        cb.lessThanOrEqualTo(root.get("price"), maxPrice));
    }

    if (search != null && !search.trim().isEmpty()) {
      spec = spec.and((root, query, cb) ->
        cb.or(
          cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
          cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")
        ));
    }

    return productRepository.findAll(spec, pageable)
      .map(productMapper::entityToDto);
  }

  public ProductDto getProduct(Long id){
    Product product = productRepository.findById(id)
      .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    return productMapper.entityToDto(product);
  }

  private String saveImage(MultipartFile image) throws IOException {
    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
    Path filePath = Paths.get(fileStorageConfig.getDirectory(), fileName);

    Files.createDirectories(filePath.getParent());
    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return fileStorageConfig.getBaseUrl() + "/" + fileName;
  }

  private void deleteImageFile(String imageUrl) {
    try {
      // Extract filename from the URL
      String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
      Path filePath = Paths.get(fileStorageConfig.getDirectory(), fileName);

      // Delete the file if it exists
      if (Files.exists(filePath)) {
        Files.delete(filePath);
        log.info("Deleted image file: {}", filePath);
      }
    } catch (IOException e) {
      log.error("Failed to delete image file: {}", imageUrl, e);
    }
  }
}
