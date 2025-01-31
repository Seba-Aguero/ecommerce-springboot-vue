package ecommerce_springboot_vue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ecommerce_springboot_vue.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

}
