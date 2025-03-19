package ecommerce_springboot_vue.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.mapper.ProductMapper;
import ecommerce_springboot_vue.repository.ICategoryRepository;
import ecommerce_springboot_vue.repository.IProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private IProductRepository productRepository;

  @Mock
  private ICategoryRepository categoryRepository;

  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductService productService;

  private Product testProduct;
  private ProductDto testProductDto;

  @BeforeEach
  void setUp() {
    testProduct = Product.builder()
      .id(1L)
      .name("Test Product")
      .description("Test Description")
      .price(BigDecimal.TEN)
      .totalStock(10)
      .build();

    testProductDto = ProductDto.builder()
      .id(1L)
      .name("Test Product")
      .description("Test Description")
      .price(BigDecimal.TEN)
      .totalStock(10)
      .build();
  }

  @Test
  void createProductSuccess() throws IOException {
    when(productMapper.dtoToEntity(testProductDto)).thenReturn(testProduct);
    when(productRepository.save(any(Product.class))).thenReturn(testProduct);
    when(productMapper.entityToDto(testProduct)).thenReturn(testProductDto);

    ProductDto result = productService.createProduct(testProductDto, null);

    assertNotNull(result);
    assertEquals(testProductDto.getName(), result.getName());
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void updateProductSuccess() throws IOException {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(productRepository.save(any(Product.class))).thenReturn(testProduct);
    when(productMapper.entityToDto(testProduct)).thenReturn(testProductDto);

    ProductDto result = productService.updateProduct(1L, testProductDto, null);

    assertNotNull(result);
    assertEquals(testProductDto.getName(), result.getName());
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void deleteProductSuccess() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

    productService.deleteProduct(1L);

    verify(productRepository).deleteById(1L);
  }

  @Test
  void getProductSuccess() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(productMapper.entityToDto(testProduct)).thenReturn(testProductDto);

    ProductDto result = productService.getProduct(1L);

    assertNotNull(result);
    assertEquals(testProductDto.getName(), result.getName());
  }

  @SuppressWarnings("unchecked")
  @Test
  void getAllProductsSuccess() {
    Page<Product> productPage = new PageImpl<>(List.of(testProduct));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
    when(productMapper.entityToDto(testProduct)).thenReturn(testProductDto);

    Page<ProductDto> result = productService.getAllProducts(null, null, null, null, PageRequest.of(0, 10));

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
  }
}
