package ecommerce_springboot_vue.service;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.dto.OrderItemDto;
import ecommerce_springboot_vue.exception.InsufficientStockException;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.CartMapper;
import ecommerce_springboot_vue.mapper.OrderItemMapper;
import ecommerce_springboot_vue.mapper.OrderMapper;
import ecommerce_springboot_vue.entity.*;
import ecommerce_springboot_vue.repository.IOrderItemRepository;
import ecommerce_springboot_vue.repository.IOrderRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
	private final IOrderRepository orderRepository;
	private final CartService cartService;
	private final IUserRepository userRepository;
	private final IProductRepository productRepository;
  private final IOrderItemRepository orderItemRepository;
	private final EmailService emailService;
	private final OrderMapper orderMapper;
	private final CartMapper cartMapper;
  private final OrderItemMapper orderItemMapper;

  @Value("${app.order.shipping-cost}")
  private BigDecimal shippingCost;

	public OrderDto createOrder(Long userId, String address, String phone){

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		CartDto cartDto = cartService.getCartByUserId(userId);
		Cart cart = cartMapper.dtoToEntity(cartDto);
		if(cart.getCartItems().isEmpty()){
			throw new IllegalStateException("Can not create an order with an empty cart");
		}

		Order order = Order.builder()
			.user(user)
			.address(address)
			.phone(phone)
			.status(Order.OrderStatus.PREPARING)
			.build();

		List<OrderItem> orderItems = createOrderItems(cart, order);

    BigDecimal subtotal = calculateSubtotal(orderItems);
    order.setTotalAmount(subtotal.add(shippingCost));

		Order savedOrder = orderRepository.save(order);

    // Save order items, that already have their order set
    orderItemRepository.saveAll(orderItems);

		cartService.clearCart(userId);

		try{
			emailService.sendOrderConfirmation(savedOrder);
		}catch (MailException e){
      throw new IllegalStateException("Failed to send order confirmation email");
		}

		return orderMapper.entityToDto(savedOrder);
	}

	private List<OrderItem> createOrderItems(Cart cart, Order order){
		return cart.getCartItems().stream().map(cartItem -> {

			Product product = productRepository.findById(cartItem.getProduct().getId())
				.orElseThrow(() -> {
					return new EntityNotFoundException("Product not found with id: " + cartItem.getProduct().getId());
				});

			if(product.getPrice() == null){
				throw new IllegalStateException("Price is not set for product "+product.getName());
			}
			if(product.getTotalStock() == null){
				throw new IllegalStateException("Product quantity is not set for product "+product.getName());
			}
			if(product.getTotalStock() < cartItem.getQuantity()){
				throw new InsufficientStockException("Not enough stock for product "+product.getName());
			}

			product.setTotalStock(product.getTotalStock() - cartItem.getQuantity());

      productRepository.save(product);

			return OrderItem.builder()
				.order(order)
				.product(product)
				.quantity(cartItem.getQuantity())
				.price(product.getPrice())
				.build();

		}).toList();
	}

  private BigDecimal calculateSubtotal(List<OrderItem> orderItems) {
    return orderItems.stream()
      .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	public List<OrderDto> getAllOrders(){
		return orderRepository.findAll().stream()
			.map(orderMapper::entityToDto)
			.toList();
	}

	public List<OrderDto> getUserOrders(Long userId){
		if (!userRepository.existsById(userId)) {
			throw new ResourceNotFoundException("User not found with id: " + userId);
    }

		List<Order> orders = orderRepository.findByUserId(userId);
		return orders.stream()
			.map(orderMapper::entityToDto)
			.toList();
	}

	public OrderDto updateOrderStatus(Long orderId, Order.OrderStatus status){
		Order order = orderRepository.findById(orderId)
			.orElseThrow(()->new ResourceNotFoundException("Order not found"));

		order.setStatus(status);
		Order updatedOrder = orderRepository.save(order);
		return orderMapper.entityToDto(updatedOrder);
	}

	public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
    // Check if the order exists
		orderRepository.findById(orderId)
			.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
		return orderItems.stream()
			.map(orderItemMapper::entityToDto)
			.collect(Collectors.toList());
	}
}
