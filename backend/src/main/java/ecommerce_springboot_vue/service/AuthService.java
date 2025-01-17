package ecommerce_springboot_vue.service;

import java.security.SecureRandom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce_springboot_vue.dto.request.user.ChangePasswordRequest;
import ecommerce_springboot_vue.dto.request.user.LoginRequest;
import ecommerce_springboot_vue.dto.response.AuthResponse;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.exception.BadRequestException;
import ecommerce_springboot_vue.mapper.UserMapper;
import ecommerce_springboot_vue.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final JwtService jwtService;
  private final EmailService emailService;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final IUserRepository userRepository;

  public AuthResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          request.getEmail(),
          request.getPassword()
        )
      );
      User user = userService.getUserByEmail(request.getEmail());
      if (!user.isEmailConfirmation()) {
        throw new BadRequestException("Please confirm your email before logging in");
      }
      String token = jwtService.generateToken(user);
      return AuthResponse.builder()
        .token(token)
        .user(userMapper.entityToDto(user))
        .build();
    } catch (BadCredentialsException e) {
      throw new BadRequestException("Invalid email or password");
    }
  }

  public User register(User user) {
    if(userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new IllegalStateException("Email already taken");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(User.Role.USER);
    user.setConfirmationCode(generateConfirmationCode());
    user.setEmailConfirmation(false);
    emailService.sendConfirmationCode(user);
    return userRepository.save(user);
  }

  private String generateConfirmationCode() {
    SecureRandom secureRandom = new SecureRandom();
    return String.format("%06d", secureRandom.nextInt(1000000));
  }

  public void changePassword(String email, ChangePasswordRequest request){
    if(!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new BadCredentialsException("Passwords do not match");
    }

    User user = userService.getUserByEmail(email);
    if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }

  public void confirmEmail(String email, String confirmationCode){
    User user = userService.getUserByEmail(email);
    if (user.isEmailConfirmation()) {
      throw new BadCredentialsException("User has already confirmed their email");
    }
    if(user.getConfirmationCode() != null && user.getConfirmationCode().equals(confirmationCode)){
      user.setEmailConfirmation(true);
      user.setConfirmationCode(null);
      userRepository.save(user);
    }
    else{
      throw new BadCredentialsException("Invalid confirmation code");
    }
  }
}