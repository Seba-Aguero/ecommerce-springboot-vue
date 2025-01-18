package ecommerce_springboot_vue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "Cart related endpoints")
public class CartController {

  private final CartService cartService;

  @GetMapping("/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
    CartDto cartDto = cartService.getCartByUserId(userId);
    return ResponseEntity.ok(cartDto);
  }

  @GetMapping("/{userId}/items")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartDto> addToCart(
    @PathVariable Long userId,
    @RequestParam Long productId,
    @RequestParam @PositiveOrZero Integer quantity)
  {
    CartDto cartDto = cartService.addToCart(userId, productId, quantity);
    return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
    cartService.clearCart(userId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{userId}/items/{productId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> removeCartItem(@PathVariable Long userId, @PathVariable Long productId) {
    cartService.removeCartItem(userId, productId);
    return ResponseEntity.noContent().build();
  }
}
