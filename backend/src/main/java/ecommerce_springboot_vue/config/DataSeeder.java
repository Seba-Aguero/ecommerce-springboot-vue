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
import java.util.ArrayList;
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
      .orElseThrow(()-> new RuntimeException("Category not found"));

    Category clothing = categories.stream()
      .filter(c -> c.getName().equals("Clothing"))
      .findFirst()
      .orElseThrow(()-> new RuntimeException("Category not found"));

    Category books = categories.stream()
      .filter(c -> c.getName().equals("Books"))
      .findFirst()
      .orElseThrow(()-> new RuntimeException("Category not found"));

    List<Product> products = new ArrayList<>();

    // Electronics
    products.add(Product.builder()
      .name("Smartphone X")
      .description("Latest generation smartphone with 5G")
      .price(new BigDecimal("999.99"))
      .quantity(50)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/smartphone.webp")
      .build());

    products.add(Product.builder()
      .name("Laptop Pro")
      .description("Professional laptop for developers")
      .price(new BigDecimal("1499.99"))
      .quantity(30)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/laptop.webp")
      .build());

    products.add(Product.builder()
      .name("Wireless Earbuds")
      .description("High-quality wireless earbuds with noise cancellation")
      .price(new BigDecimal("199.99"))
      .quantity(100)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/earbuds.jpg")
      .build());

    products.add(Product.builder()
      .name("4K Smart TV")
      .description("55-inch 4K Smart TV with HDR")
      .price(new BigDecimal("699.99"))
      .quantity(25)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/tv.jpg")
      .build());

    products.add(Product.builder()
      .name("Gaming Console")
      .description("Next-gen gaming console with 4K graphics")
      .price(new BigDecimal("499.99"))
      .quantity(40)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/console.jpg")
      .build());

    products.add(Product.builder()
      .name("Tablet Pro")
      .description("Professional tablet with stylus support")
      .price(new BigDecimal("799.99"))
      .quantity(35)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/tablet.jpg")
      .build());

    products.add(Product.builder()
      .name("Wireless Keyboard")
      .description("Mechanical wireless keyboard with RGB lighting")
      .price(new BigDecimal("149.99"))
      .quantity(45)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/keyboard.jpg")
      .build());

    products.add(Product.builder()
      .name("Digital Drawing Tablet")
      .description("Professional drawing tablet for digital artists")
      .price(new BigDecimal("299.99"))
      .quantity(30)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/drawing-tablet.jpg")
      .build());

    products.add(Product.builder()
      .name("Wireless Mouse")
      .description("Ergonomic wireless mouse with precision tracking")
      .price(new BigDecimal("79.99"))
      .quantity(65)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/mouse.jpg")
      .build());

    products.add(Product.builder()
      .name("Bluetooth Speaker")
      .description("Portable waterproof bluetooth speaker with 24h battery")
      .price(new BigDecimal("159.99"))
      .quantity(40)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/speaker.jpg")
      .build());

    products.add(Product.builder()
      .name("Webcam Pro")
      .description("4K webcam with auto-focus and noise-canceling microphone")
      .price(new BigDecimal("129.99"))
      .quantity(35)
      .categories(new HashSet<>(Arrays.asList(electronics)))
      .imageUrl("/images/webcam.jpg")
      .build());

    // Clothing
    products.add(Product.builder()
      .name("Cotton T-Shirt")
      .description("Comfortable cotton t-shirt")
      .price(new BigDecimal("29.99"))
      .quantity(100)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/t-shirt.jpg")
      .build());

    products.add(Product.builder()
      .name("Denim Jeans")
      .description("Classic fit denim jeans")
      .price(new BigDecimal("59.99"))
      .quantity(80)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/jeans.jpg")
      .build());

    products.add(Product.builder()
      .name("Winter Jacket")
      .description("Warm winter jacket with water resistance")
      .price(new BigDecimal("129.99"))
      .quantity(45)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/jacket.jpg")
      .build());

    products.add(Product.builder()
      .name("Running Shoes")
      .description("Lightweight running shoes with cushioning")
      .price(new BigDecimal("89.99"))
      .quantity(60)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/shoes.jpg")
      .build());

    products.add(Product.builder()
      .name("Summer Dress")
      .description("Floral summer dress")
      .price(new BigDecimal("49.99"))
      .quantity(70)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/dress.jpg")
      .build());

    products.add(Product.builder()
      .name("Sports Shorts")
      .description("Breathable sports shorts for running and gym")
      .price(new BigDecimal("34.99"))
      .quantity(120)
      .categories(new HashSet<>(Arrays.asList(clothing)))
      .imageUrl("/images/shorts.jpg")
      .build());

    // Books
    products.add(Product.builder()
      .name("Programming Guide")
      .description("Complete programming guide for beginners")
      .price(new BigDecimal("49.99"))
      .quantity(75)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/programming-book.jpg")
      .build());

    products.add(Product.builder()
      .name("Science Fiction Novel")
      .description("Bestselling sci-fi novel")
      .price(new BigDecimal("19.99"))
      .quantity(90)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/scifi-book.jpg")
      .build());

    products.add(Product.builder()
      .name("Cookbook")
      .description("International cuisine cookbook")
      .price(new BigDecimal("34.99"))
      .quantity(55)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/cookbook.jpg")
      .build());

    products.add(Product.builder()
      .name("History Book")
      .description("Comprehensive world history book")
      .price(new BigDecimal("39.99"))
      .quantity(65)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/history-book.jpg")
      .build());

    products.add(Product.builder()
      .name("Self-Help Book")
      .description("Personal development guide")
      .price(new BigDecimal("24.99"))
      .quantity(85)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/selfhelp-book.jpg")
      .build());

    products.add(Product.builder()
      .name("Photography Guide")
      .description("Complete guide to digital photography")
      .price(new BigDecimal("44.99"))
      .quantity(50)
      .categories(new HashSet<>(Arrays.asList(books)))
      .imageUrl("/images/photo-book.jpg")
      .build());

    // Mixed Categories (Electronics + Books)
    products.add(Product.builder()
      .name("E-Reader")
      .description("Digital e-reader with backlight")
      .price(new BigDecimal("129.99"))
      .quantity(40)
      .categories(new HashSet<>(Arrays.asList(electronics, books)))
      .imageUrl("/images/ereader.jpg")
      .build());

    // Mixed Categories (Electronics + Clothing)
    products.add(Product.builder()
      .name("Smart Watch")
      .description("Fitness tracking smart watch")
      .price(new BigDecimal("199.99"))
      .quantity(55)
      .categories(new HashSet<>(Arrays.asList(electronics, clothing)))
      .imageUrl("/images/smartwatch.jpg")
      .build());

    products.add(Product.builder()
      .name("Smart Glasses")
      .description("AR-enabled smart glasses with HD display")
      .price(new BigDecimal("599.99"))
      .quantity(25)
      .categories(new HashSet<>(Arrays.asList(electronics, clothing)))
      .imageUrl("/images/smart-glasses.jpg")
      .build());

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