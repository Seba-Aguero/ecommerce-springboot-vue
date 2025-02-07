package ecommerce_springboot_vue.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.entity.Cart;
import ecommerce_springboot_vue.entity.CartItem;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.enums.CartOperation;
import ecommerce_springboot_vue.mapper.CartMapper;
import ecommerce_springboot_vue.repository.ICartRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @Mock
  private ICartRepository cartRepository;

  @Mock
  private IProductRepository productRepository;

  @Mock
  private IUserRepository userRepository;

  @Mock
  private CartMapper cartMapper;

  @InjectMocks
  private CartService cartService;

  private Cart testCart;
  private CartDto testCartDto;
  private User testUser;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
      .id(1L)
      .email("test@test.com")
      .build();

    testProduct = Product.builder()
      .id(1L)
      .name("Test Product")
      .quantity(10)
      .build();

    testCart = Cart.builder()
      .id(1L)
      .user(testUser)
      .cartItems(new ArrayList<>())
      .build();

    testCartDto = CartDto.builder()
      .id(1L)
      .userId(1L)
      .cartItems(new ArrayList<>())
      .build();
  }

  @Test
  void getCartByUserIdSuccess() {
    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
    when(cartMapper.entityToDto(testCart)).thenReturn(testCartDto);

    CartDto result = cartService.getCartByUserId(1L);

    assertNotNull(result);
    assertEquals(testCartDto.getId(), result.getId());
  }

  @Test
  void addToCartSuccess() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
    when(cartMapper.entityToDto(testCart)).thenReturn(testCartDto);

    CartDto result = cartService.addToCart(1L, 1L, 1);

    assertNotNull(result);
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void removeCartItemSuccess() {
    CartItem cartItem = CartItem.builder()
      .product(testProduct)
      .build();
    testCart.getCartItems().add(cartItem);

    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

    cartService.removeCartItem(1L, 1L);

    verify(cartRepository).save(testCart);
    assertTrue(testCart.getCartItems().isEmpty());
  }

  @Test
  void updateQuantityIncrementSuccess() {
    CartItem cartItem = CartItem.builder()
      .product(testProduct)
      .quantity(1)
      .cart(testCart)
      .build();
    testCart.getCartItems().add(cartItem);

    CartDto expectedDto = CartDto.builder()
      .id(1L)
      .userId(1L)
      .build();

    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
    when(cartMapper.entityToDto(any(Cart.class))).thenReturn(expectedDto);

    CartDto result = cartService.updateQuantity(1L, 1L, CartOperation.INCREMENT);

    assertNotNull(result);
    assertEquals(2, cartItem.getQuantity());
    verify(cartRepository).save(testCart);
    verify(cartMapper).entityToDto(testCart);
  }

  @Test
  void updateQuantityDecrementSuccess() {
    CartItem cartItem = CartItem.builder()
      .product(testProduct)
      .quantity(2)
      .build();
    testCart.getCartItems().add(cartItem);

    CartDto expectedDto = CartDto.builder()
      .id(1L)
      .userId(1L)
      .build();

    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
    when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
    when(cartMapper.entityToDto(any(Cart.class))).thenReturn(expectedDto);

    CartDto result = cartService.updateQuantity(1L, 1L, CartOperation.DECREMENT);

    assertNotNull(result);
    assertEquals(1, cartItem.getQuantity());
    verify(cartRepository).save(testCart);
    verify(cartMapper).entityToDto(testCart);
  }

  @Test
  void clearCartSuccess() {
    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

    cartService.clearCart(1L);

    verify(cartRepository).save(testCart);
    assertTrue(testCart.getCartItems().isEmpty());
  }
}