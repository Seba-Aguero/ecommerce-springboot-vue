package ecommerce_springboot_vue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce_springboot_vue.entity.User;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}