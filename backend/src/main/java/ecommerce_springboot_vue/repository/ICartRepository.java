package ecommerce_springboot_vue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_springboot_vue.entity.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByUserId(Long userId);
}
