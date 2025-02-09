package ecommerce_springboot_vue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce_springboot_vue.dto.UserDto;
import ecommerce_springboot_vue.dto.request.user.ChangePasswordRequest;
import ecommerce_springboot_vue.dto.request.user.EmailConfirmationRequest;
import ecommerce_springboot_vue.dto.request.user.LoginRequest;
import ecommerce_springboot_vue.dto.request.user.RegisterRequest;
import ecommerce_springboot_vue.dto.response.AuthResponse;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.mapper.UserMapper;
import ecommerce_springboot_vue.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ecommerce_springboot_vue.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth related endpoints")
public class AuthController {

  private final AuthService authService;
  private final UserMapper userMapper;
  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request){
    log.info("Received registration request for email: {}", request.getEmail());
    if (!request.getPassword().equals(request.getConfirmPassword())) {
        throw new BadRequestException("Passwords do not match");
    }
    User user = User.builder()
      .email(request.getEmail())
      .password(request.getPassword())
      .build();
    UserDto RegisteredUserDto = userMapper.entityToDto(authService.register(user));
    log.info("User registered successfully: {}", RegisteredUserDto.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body(RegisteredUserDto);
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    authService.changePassword(email, request);
    return ResponseEntity.ok().body("Password changed successfully");
  }

  @PostMapping("/confirm-email")
  public ResponseEntity<AuthResponse> confirmEmail(@Valid @RequestBody EmailConfirmationRequest request){
    authService.confirmEmail(request.getEmail(), request.getConfirmationCode());

    // After confirming email, login the user
    LoginRequest loginRequest = LoginRequest.builder()
      .email(request.getEmail())
      .password(request.getPassword())
      .build();
    AuthResponse authResponse = authService.login(loginRequest);

    return ResponseEntity.ok(authResponse);
  }

}
