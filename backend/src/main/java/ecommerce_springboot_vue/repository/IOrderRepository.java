package ecommerce_springboot_vue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_springboot_vue.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserId(Long userId);
}
