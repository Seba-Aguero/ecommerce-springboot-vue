package ecommerce_springboot_vue.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.entity.Cart;
import ecommerce_springboot_vue.entity.CartItem;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.enums.CartOperation;
import ecommerce_springboot_vue.exception.InsufficientStockException;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.CartMapper;
import ecommerce_springboot_vue.repository.ICartRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

  private final ICartRepository cartRepository;
  private final CartMapper cartMapper;
  private final IProductRepository productRepository;
  private final IUserRepository userRepository;

  public CartDto getCartByUserId(Long userId) {
    Cart cart = cartRepository.findByUserId(userId)
      .orElseThrow(() -> new RuntimeException("Cart not found"));

    return cartMapper.entityToDto(cart);
  }

  public CartDto addToCart(Long userId, Long productId, Integer quantity) {
    Product product = productRepository.findById(productId)
      .orElseThrow(()->new ResourceNotFoundException("Product not found"));

    if(product.getTotalStock() < quantity){
      throw new InsufficientStockException("Not enough stock available");
    }

    User user = userRepository.findById(userId)
      .orElseThrow(()->new ResourceNotFoundException("User not found"));

    Cart cart = cartRepository.findByUserId(userId)
      .orElse(Cart.builder()
        .user(user)
        .build());

    Optional<CartItem> existingCartItem = cart.getCartItems().stream()
      .filter(item -> item.getProduct().getId().equals(productId))
      .findFirst();

    if(existingCartItem.isPresent()){
      CartItem cartItem = existingCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity()+quantity);
    }else{
      CartItem cartItem = CartItem.builder()
      .cart(cart)
      .product(product)
      .quantity(quantity)
      .build();
      cart.getCartItems().add(cartItem);
    }

    Cart savedCart = cartRepository.save(cart);
    return cartMapper.entityToDto(savedCart);
  }

  public CartDto updateQuantity(Long userId, Long productId, CartOperation operation) {
    Cart cart = cartRepository.findByUserId(userId)
      .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    CartItem cartItem = cart.getCartItems().stream()
      .filter(item -> item.getProduct().getId().equals(productId))
      .findFirst()
      .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

    switch (operation) {
      case INCREMENT:
        if (cartItem.getQuantity() + 1 > cartItem.getProduct().getTotalStock()) {
          throw new InsufficientStockException("Not enough stock available");
        }
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        break;
      case DECREMENT:
        if (cartItem.getQuantity() > 1) {
          cartItem.setQuantity(cartItem.getQuantity() - 1);
        }
        break;
    }

    Cart savedCart = cartRepository.save(cart);
    return cartMapper.entityToDto(savedCart);
  }

  public void removeCartItem(Long userId, Long productId) {
    Cart cart = cartRepository.findByUserId(userId)
      .orElseThrow(() -> new RuntimeException("Cart not found for user"));

    cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

    cartRepository.save(cart);
  }

  public void clearCart(Long userId){
    Cart cart = cartRepository.findByUserId(userId)
      .orElseThrow(()->new RuntimeException("Cart not found"));

    cart.getCartItems().clear();
    cartRepository.save(cart);
  }
}
