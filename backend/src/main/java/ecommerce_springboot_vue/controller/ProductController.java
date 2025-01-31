package ecommerce_springboot_vue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Products related endpoints")
public class ProductController {

  private final ProductService productService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductDto> createProduct(
    @RequestPart("product") @Valid ProductDto productDto,
    @RequestPart(value = "image", required = false) MultipartFile image) throws IOException
  {
    ProductDto createdProductDto = productService.createProduct(productDto, image);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDto);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductDto> updateProduct(
    @PathVariable Long id,
    @RequestPart("product") @Valid ProductDto productDto,
    @RequestPart(value = "image", required = false) MultipartFile image) throws IOException
  {
    ProductDto updatedProductDto = productService.updateProduct(id, productDto, image);
    return ResponseEntity.ok(updatedProductDto);
  }

  @GetMapping
  public ResponseEntity<Page<ProductDto>> getAllProducts(
    @RequestParam(required = false) List<Long> categories,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice,
    @RequestParam(required = false) String search,
    @PageableDefault(size=9) Pageable pageable)
  {
    Page<ProductDto> productDtos = productService.getAllProducts(categories, minPrice, maxPrice, search, pageable);
    return ResponseEntity.ok(productDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
    ProductDto productDto = productService.getProduct(id);
    return ResponseEntity.ok(productDto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

}
