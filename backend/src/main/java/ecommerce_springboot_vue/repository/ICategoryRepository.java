package ecommerce_springboot_vue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_springboot_vue.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

}
