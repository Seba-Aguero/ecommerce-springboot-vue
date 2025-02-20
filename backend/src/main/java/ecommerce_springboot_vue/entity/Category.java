package ecommerce_springboot_vue.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
// import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder.Default;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;

@Entity
@Getter
@Setter
@ToString(exclude = "products")  // Exclude products from ToString
@EqualsAndHashCode(exclude = "products")  // Exclude products from EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "Name can not be blank")
  @Length(max = 50, message = "Name can not be longer than 50 characters")
  private String name;

  @Default
  @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
  private Set<Product> products = new HashSet<>();

  @Default
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Default
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();
}
