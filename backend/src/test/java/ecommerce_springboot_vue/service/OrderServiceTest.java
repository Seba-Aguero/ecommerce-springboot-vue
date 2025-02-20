package ecommerce_springboot_vue.service;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.CartItemDto;
import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.dto.ProductDto;
import ecommerce_springboot_vue.entity.Cart;
import ecommerce_springboot_vue.entity.CartItem;
import ecommerce_springboot_vue.entity.Order;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.mapper.CartMapper;
import ecommerce_springboot_vue.mapper.OrderMapper;
import ecommerce_springboot_vue.repository.IOrderItemRepository;
import ecommerce_springboot_vue.repository.IOrderRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock
  private IOrderRepository orderRepository;

  @Mock
  private IOrderItemRepository orderItemRepository;

  @Mock
  private IUserRepository userRepository;

  @Mock
  private IProductRepository productRepository;

  @Mock
  private CartService cartService;

  @Mock
  private EmailService emailService;

  @Mock
  private OrderMapper orderMapper;

  @Mock
  private CartMapper cartMapper;

  @InjectMocks
  private OrderService orderService;

  private Order testOrder;
  private OrderDto testOrderDto;
  private User testUser;
  private Cart testCart;
  private CartDto testCartDto;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
      .id(1L)
      .email("test@test.com")
      .emailConfirmation(true)
      .build();

    testOrder = Order.builder()
      .id(1L)
      .user(testUser)
      .status(Order.OrderStatus.PREPARING)
      .address("Test Address")
      .phone("123456789")
      .build();

    testOrderDto = OrderDto.builder()
      .id(1L)
      .userId(1L)
      .status(Order.OrderStatus.PREPARING)
      .address("Test Address")
      .phone("123456789")
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
  void createOrderSuccess() {
    Product testProduct = Product.builder()
      .id(1L)
      .name("Test Product")
      .price(BigDecimal.TEN)
      .totalStock(5)
      .build();
    ProductDto testProductDto = ProductDto.builder()
      .id(1L)
      .name("Test Product")
      .price(BigDecimal.TEN)
      .totalStock(5)
      .build();
    CartItem cartItem = CartItem.builder()
      .product(testProduct)
      .quantity(2)
      .build();
    testCart.getCartItems().add(cartItem);
    testCartDto.getCartItems().add(CartItemDto.builder()
      .product(testProductDto)
      .quantity(2)
      .build());
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(cartService.getCartByUserId(1L)).thenReturn(testCartDto);
    when(cartMapper.dtoToEntity(testCartDto)).thenReturn(testCart);
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
    when(orderMapper.entityToDto(testOrder)).thenReturn(testOrderDto);

    OrderDto result = orderService.createOrder(1L, "Test Address", "123456789");

    assertNotNull(result);
    assertEquals(testOrderDto.getAddress(), result.getAddress());
    verify(orderRepository).save(any(Order.class));
    verify(orderItemRepository).saveAll(anyList());
    verify(cartService).clearCart(1L);
    verify(emailService).sendOrderConfirmation(any(Order.class));
    verify(productRepository).findById(1L);
  }

  @Test
  void getAllOrdersSuccess() {
    when(orderRepository.findAll()).thenReturn(List.of(testOrder));
    when(orderMapper.entityToDto(testOrder)).thenReturn(testOrderDto);

    List<OrderDto> result = orderService.getAllOrders();

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void getUserOrdersSuccess() {
    when(userRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.findByUserId(1L)).thenReturn(List.of(testOrder));
    when(orderMapper.entityToDto(testOrder)).thenReturn(testOrderDto);

    List<OrderDto> result = orderService.getUserOrders(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void updateOrderStatusSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
    when(orderMapper.entityToDto(testOrder)).thenReturn(testOrderDto);

    OrderDto result = orderService.updateOrderStatus(1L, Order.OrderStatus.DELIVERED);

    assertNotNull(result);
    assertEquals(Order.OrderStatus.DELIVERED, testOrder.getStatus());
  }
}
