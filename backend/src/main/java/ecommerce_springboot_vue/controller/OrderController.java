package ecommerce_springboot_vue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import ecommerce_springboot_vue.dto.OrderDto;
import ecommerce_springboot_vue.dto.OrderItemDto;
import ecommerce_springboot_vue.dto.request.order.CreateOrderRequest;
import ecommerce_springboot_vue.dto.request.order.UpdateOrderStatusRequest;
import ecommerce_springboot_vue.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Orders related endpoints")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
    OrderDto createrOrderDto = orderService.createOrder(
      request.getUserId(),
      request.getAddress(),
      request.getPhone()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createrOrderDto);
  }

  // Here I could add these parameters to have optional filters:
  // @RequestParam(required = false) Order.OrderStatus status,
  // @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
  @GetMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<OrderDto>> getAllOrders() {
    List<OrderDto> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Long userId) {
    List<OrderDto> orders = orderService.getUserOrders(userId);
    return ResponseEntity.ok(orders);
  }

  @PutMapping("/{orderId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderDto> updateOrderStatus(
    @PathVariable Long orderId,
    @RequestBody UpdateOrderStatusRequest request)
  {
    OrderDto updatedOrderDto = orderService.updateOrderStatus(
      orderId,
      request.getStatus()
    );
    return ResponseEntity.ok(updatedOrderDto);
  }

  @GetMapping("/{orderId}/items")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Long orderId) {
    List<OrderItemDto> orderItems = orderService.getOrderItemsByOrderId(orderId);
    return ResponseEntity.ok(orderItems);
  }
}
