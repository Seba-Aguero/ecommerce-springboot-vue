package ecommerce_springboot_vue.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder.Default;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @ToString.Exclude
  private User user;

  @Column(nullable = false)
  @NotBlank(message = "Address can not be blank")
  @Length(max = 50, message = "Address can not be longer than 50 characters")
  private String address;

  @Column(nullable = false)
  @NotBlank(message = "Phone can not be blank")
  @Length(max = 15, message = "Phone can not be longer than 15 characters")
  private String phone;

  @Column(nullable = false)
  @PositiveOrZero(message = "Total amount can not be negative")
  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public enum OrderStatus {
    PREPARING, DELIVERING, DELIVERED, CANCELED
  }

  @Default
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Default
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();
}
