package ecommerce_springboot_vue.service;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.exception.InsufficientStockException;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.CartMapper;
import ecommerce_springboot_vue.mapper.OrderMapper;
import ecommerce_springboot_vue.entity.*;
import ecommerce_springboot_vue.repository.IOrderRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final Logger logger = LoggerFactory.getLogger(OrderService.class);

	private final IOrderRepository orderRepository;
	private final CartService cartService;
	private final IUserRepository userRepository;
	private final IProductRepository productRepository;
	private final EmailService emailService;
	private final OrderMapper orderMapper;
	private final CartMapper cartMapper;

	public OrderDto createOrder(Long userId, String address, String phone){
		User user = userRepository.findById(userId)
			.orElseThrow(()->new ResourceNotFoundException("User not found"));
		if(!user.isEmailConfirmation()){
			throw new IllegalStateException("Email not confirmed. Please confirm email before placing order");
		}
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
		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(userId);

		try{
			emailService.sendOrderConfirmation(savedOrder);
		}catch (MailException e){
			logger.error("Failed to send order confirmation email for order ID "+savedOrder.getId(), e);
		}
		return orderMapper.entityToDto(savedOrder);
	}

	private List<OrderItem> createOrderItems(Cart cart, Order order){
		return cart.getCartItems().stream().map(cartItem -> {
			Product product = productRepository.findById(cartItem.getProduct().getId())
				.orElseThrow(()-> new EntityNotFoundException("Product not found with id: "+cartItem.getProduct().getId()));

			if(product.getQuantity() == null){
				throw new IllegalStateException("Product quantity is not set for product "+product.getName());
			}
			if(product.getQuantity() < cartItem.getQuantity()){
				throw new InsufficientStockException("Not enough stock for product "+product.getName());
			}
			product.setQuantity(product.getQuantity() - cartItem.getQuantity());
			productRepository.save(product);

			return OrderItem.builder()
				.order(order)
				.product(product)
				.quantity(cartItem.getQuantity())
				.price(product.getPrice())
				.build();

		}).toList();
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
}