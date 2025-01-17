package ecommerce_springboot_vue.service;

import ecommerce_springboot_vue.dto.CategoryDto;
import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.entity.Category;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.ProductMapper;
import ecommerce_springboot_vue.repository.ICategoryRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
  private final IProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ICategoryRepository categoryRepository;

  private static final String UPLOAD_DIR = "src/main/resources/static/images/";

  public ProductDto createProduct(ProductDto productDto, MultipartFile image) throws IOException{
    Product product = productMapper.dtoToEntity(productDto);

    if(image != null && !image.isEmpty()){
      String fileName = saveImage(image);
      product.setImageUrl("/images/" + fileName);
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
    if (productDto.getQuantity() != null && productDto.getQuantity() < 0) {
      throw new IllegalArgumentException("Quantity can not be negative");
    }

    existingProduct.setName(productDto.getName());
    existingProduct.setDescription(productDto.getDescription());
    existingProduct.setPrice(productDto.getPrice());
    existingProduct.setQuantity(productDto.getQuantity());

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
      existingProduct.setImageUrl("/images/" + fileName);
    }

    Product updatedProduct = productRepository.save(existingProduct);
    return productMapper.entityToDto(updatedProduct);
  }

  public void deleteProduct(Long id){
    if(!productRepository.existsById(id)){
      throw new ResourceNotFoundException("Product not found");
    }
    productRepository.deleteById(id);
  }

  public Page<ProductDto> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable)
      .map(productMapper::entityToDto);
  }

  public ProductDto getProduct(Long id){
    Product product = productRepository.findById(id)
      .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    return productMapper.entityToDto(product);
  }

  private String saveImage(MultipartFile image) throws IOException{
    String fileName = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
    Path path = Paths.get(UPLOAD_DIR + fileName);
    Files.createDirectories(path.getParent());
    Files.write(path, image.getBytes());
    return fileName;
  }
}