package ecommerce_springboot_vue.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import ecommerce_springboot_vue.entity.Category;
import ecommerce_springboot_vue.entity.Product;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.repository.ICategoryRepository;
import ecommerce_springboot_vue.repository.IProductRepository;
import ecommerce_springboot_vue.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DataSeeder {

  private final ICategoryRepository categoryRepository;
  private final IProductRepository productRepository;
  private final IUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      try {
        log.info("Checking if database needs seeding...");

        if (userRepository.count() == 0) {
          log.info("No users found, creating users...");
          createUsers();
        }

        long categoriesCount = categoryRepository.count();
        long productsCount = productRepository.count();

        log.info("Current state - Categories: {}, Products: {}", categoriesCount, productsCount);

        if (productsCount == 0) {
          log.info("No products found, starting seeding process...");

          List<Category> categories;
          if (categoriesCount == 0) {
            categories = createCategories();
          } else {
            categories = categoryRepository.findAll();
            categories.forEach(category -> category.getProducts().size());
          }

          createAndSaveProducts(categories);

          log.info("Database seeding completed!");
        } else {
          log.info("Products already exist, skipping seeding");
        }
      } catch (Exception e) {
        log.error("Error during database seeding: ", e);
      }
    };
  }

  private List<Category> createCategories() {
    Category electronics = Category.builder()
      .name("Electronics")
      .products(new HashSet<>())
      .build();

    Category clothing = Category.builder()
      .name("Clothing")
      .products(new HashSet<>())
      .build();

    Category books = Category.builder()
      .name("Books")
      .products(new HashSet<>())
      .build();

    List<Category> categories = Arrays.asList(electronics, clothing, books);
    return categoryRepository.saveAll(categories);
  }

  private void createAndSaveProducts(List<Category> categories) {
    Category electronics = categories.stream()
      .filter(c -> c.getName().equals("Electronics"))
      .findFirst()
      .orElseThrow();

    Category clothing = categories.stream()
      .filter(c -> c.getName().equals("Clothing"))
      .findFirst()
      .orElseThrow();

    Category books = categories.stream()
      .filter(c -> c.getName().equals("Books"))
      .findFirst()
      .orElseThrow();

    // Create products with their categories from the start
    Product smartphone = Product.builder()
      .name("Smartphone X")
      .description("Latest generation smartphone")
      .price(new BigDecimal("999.99"))
      .quantity(50)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/smartphone.webp")
      .build();

    Product laptop = Product.builder()
      .name("Laptop Pro")
      .description("Professional laptop for developers")
      .price(new BigDecimal("1499.99"))
      .quantity(30)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/laptop.webp")
      .build();

    Product shirt = Product.builder()
      .name("Cotton T-Shirt")
      .description("Comfortable cotton t-shirt")
      .price(new BigDecimal("29.99"))
      .quantity(100)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/t-shirt.jpg")
      .build();

    Product programmingBook = Product.builder()
      .name("Programming Guide")
      .description("Complete programming guide for beginners")
      .price(new BigDecimal("49.99"))
      .quantity(75)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/programming-book.jpg")
      .build();

    // Save all products at once
    List<Product> products = Arrays.asList(smartphone, laptop, shirt, programmingBook);
    productRepository.saveAll(products);
    log.info("Products created successfully");
  }

  private void createUsers() {
    // Create admin user
    User admin = User.builder()
      .email("admin@example.com")
      .password(passwordEncoder.encode("admin123"))
      .role(User.Role.ADMIN)
      .emailConfirmation(true)
      .build();

    // Create normal users
    User user1 = User.builder()
      .email("user1@example.com")
      .password(passwordEncoder.encode("user123"))
      .role(User.Role.USER)
      .emailConfirmation(true)
      .build();

    User user2 = User.builder()
      .email("user2@example.com")
      .password(passwordEncoder.encode("user123"))
      .role(User.Role.USER)
      .emailConfirmation(true)
      .build();

    List<User> users = Arrays.asList(admin, user1, user2);
    userRepository.saveAll(users);
    log.info("Users created successfully");
  }
}