package ecommerce_springboot_vue.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Column(unique = true)
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  private String firstName;

  private String lastName;

  private String phone;

  private String address;

  private boolean emailConfirmation;

  @Enumerated(EnumType.STRING)
  private Role role;

  public enum Role{
    USER,
    ADMIN
  }

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Cart cart;

  private String confirmationCode;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Default
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Default
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Override
  public boolean isAccountNonExpired() {
    return true; //True since account expiration is not used in the project
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; //True since lock is not used in the project
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; //True since credentials expiration is not used in the project
  }

  @Override
  public boolean isEnabled() {
    return emailConfirmation;
  }
}
