package ecommerce_springboot_vue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.request.cart.AddCartItemRequest;
import ecommerce_springboot_vue.enums.CartOperation;
import ecommerce_springboot_vue.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
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

  @PostMapping("/{userId}/items")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartDto> addToCart(
    @PathVariable Long userId,
    @RequestBody AddCartItemRequest request)
  {
    CartDto cartDto = cartService.addToCart(
      userId,
      request.getProductId(),
      request.getQuantity()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
  }

  @PatchMapping("/{userId}/items/{productId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartDto> updateQuantity(
    @PathVariable Long userId,
    @PathVariable Long productId,
    @RequestParam CartOperation operation)
  {
    CartDto cartDto = cartService.updateQuantity(userId, productId, operation);
    return ResponseEntity.ok(cartDto);
  }

  @DeleteMapping("/{userId}/items/{productId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> removeCartItem(@PathVariable Long userId, @PathVariable Long productId) {
    cartService.removeCartItem(userId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
    cartService.clearCart(userId);
    return ResponseEntity.noContent().build();
  }
}
