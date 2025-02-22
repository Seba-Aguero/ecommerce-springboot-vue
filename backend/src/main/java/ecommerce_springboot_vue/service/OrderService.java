package ecommerce_springboot_vue.service;

import ecommerce_springboot_vue.dto.CartDto;
import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.dto.OrderItemDto;
import ecommerce_springboot_vue.exception.InsufficientStockException;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.CartMapper;
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

	public OrderDto createOrder(Long userId, String address, String phone){
		// log.info("Starting order creation for user ID: {}", userId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		// log.info("Found user: {}", user.getEmail());

		CartDto cartDto = cartService.getCartByUserId(userId);
		// log.info("Retrieved cart for user. Cart items count: {}", cartDto.getCartItems().size());

		Cart cart = cartMapper.dtoToEntity(cartDto);
		// log.info("Mapped CartDto to Cart entity. Items count after mapping: {}", cart.getCartItems().size());

		if(cart.getCartItems().isEmpty()){
			// log.warn("Attempted to create order with empty cart for user: {}", userId);
			throw new IllegalStateException("Can not create an order with an empty cart");
		}

		Order order = Order.builder()
			.user(user)
			.address(address)
			.phone(phone)
			.status(Order.OrderStatus.PREPARING)
			.build();

    // log.info("Creating order items from cart items. Cart items count: {}",
      // cart.getCartItems().size());

		List<OrderItem> orderItems = createOrderItems(cart, order);

    // log.info("Created order items. Order items count: {}", orderItems.size());

		// order.setOrderItems(orderItems);

    // log.info("Calculating total amount for order");

    BigDecimal totalAmount = calculateTotalAmount(orderItems);

    // log.info("Total amount calculated: {}", totalAmount);

    order.setTotalAmount(totalAmount);

    // log.info("Saving order to database");

		Order savedOrder = orderRepository.save(order);

    // Save order items, that already have their order set
    orderItemRepository.saveAll(orderItems);

    // log.info("Order saved successfully. Order ID: {}", savedOrder.getId());
    // log.info("Clearing cart for user ID: {}", userId);

		cartService.clearCart(userId);

    // log.info("Sending order confirmation email to user: {}", user.getEmail());

		try{
			emailService.sendOrderConfirmation(savedOrder);
      // log.info("Order confirmation email sent successfully");
		}catch (MailException e){
      // log.error("Failed to send order confirmation email", e);
      throw new IllegalStateException("Failed to send order confirmation email");
		}


    // OrderDto orderDto = orderMapper.entityToDto(savedOrder);

    // List<OrderItemDto> orderItemDtos = savedOrder.getOrderItems().stream()
    //   .map(orderItem -> {
    //     OrderItemDto orderItemDto = new OrderItemDto();
    //     orderItemDto.setId(orderItem.getId());
    //     orderItemDto.setOrderId(savedOrder.getId());
    //     orderItemDto.setProductId(orderItem.getProduct().getId());
    //     orderItemDto.setQuantity(orderItem.getQuantity());
    //     orderItemDto.setPrice(orderItem.getPrice());
    //     return orderItemDto;
    //   })
    //   .collect(Collectors.toList());
    // orderDto.setOrderItems(orderItemDtos);

    // return orderDto;

		return orderMapper.entityToDto(savedOrder);
	}

	private List<OrderItem> createOrderItems(Cart cart, Order order){
		// log.info("Creating order items from cart items. Cart items count: {}",
			// cart.getCartItems().size());

		return cart.getCartItems().stream().map(cartItem -> {
			// log.info("Processing cart item for product ID: {}",
				// cartItem.getProduct().getId());

			Product product = productRepository.findById(cartItem.getProduct().getId())
				.orElseThrow(() -> {
					// log.error("Product not found in database. ID: {}",
						// cartItem.getProduct().getId());
					return new EntityNotFoundException("Product not found with id: " + cartItem.getProduct().getId());
				});

			// log.info("Found product in database - ID: {}, Name: {}, Price: {}, Stock: {}",
				// product.getId(),
				// product.getName(),
				// product.getPrice(),
				// product.getTotalStock());

			if(product.getPrice() == null){
				throw new IllegalStateException("Price is not set for product "+product.getName());
			}
			if(product.getTotalStock() == null){
				throw new IllegalStateException("Product quantity is not set for product "+product.getName());
			}
			if(product.getTotalStock() < cartItem.getQuantity()){
				throw new InsufficientStockException("Not enough stock for product "+product.getName());
			}
      // log.info("Updating product stock. Current stock: {}, Quantity to subtract: {}",
        // product.getTotalStock(), cartItem.getQuantity());

			product.setTotalStock(product.getTotalStock() - cartItem.getQuantity());

      // log.info("Saving updated product stock to database");

      productRepository.save(product);

      // log.info("Product stock updated successfully. New stock: {}", product.getTotalStock());

      // log.info("Creating order item for product ID: {}", product.getId());

			return OrderItem.builder()
				.order(order)
				.product(product)
				.quantity(cartItem.getQuantity())
				.price(product.getPrice())
				.build();

		}).toList();
	}

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
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
}
