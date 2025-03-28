package ecommerce_springboot_vue.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce_springboot_vue.dto.UserDto;
import ecommerce_springboot_vue.dto.request.user.ChangePasswordRequest;
import ecommerce_springboot_vue.dto.request.user.LoginRequest;
import ecommerce_springboot_vue.dto.request.user.EmailConfirmationRequest;
import ecommerce_springboot_vue.dto.response.AuthResponse;
import ecommerce_springboot_vue.entity.Cart;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.exception.BadRequestException;
import ecommerce_springboot_vue.mapper.UserMapper;
import ecommerce_springboot_vue.repository.ICartRepository;
import ecommerce_springboot_vue.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private IUserRepository userRepository;

  @Mock
  private UserService userService;

  @Mock
  private ICartRepository cartRepository;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserMapper userMapper;

  @Mock
  private EmailService emailService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthService authService;

  private User testUser;
  private LoginRequest loginRequest;

  @BeforeEach
  void setUp() {
    // Test data
    testUser = User.builder()
      .email("test@test.com")
      .password("password123")
      .emailConfirmation(true)
      .build();

    loginRequest = LoginRequest.builder()
      .email("test@test.com")
      .password("password123")
      .build();
  }

  @Test
  void loginSuccess() {
    when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(testUser);
    when(jwtService.generateToken(testUser)).thenReturn("test.jwt.token");

    AuthResponse response = authService.login(loginRequest);

    assertNotNull(response);
    assertNotNull(response.getToken());
    assertEquals("test.jwt.token", response.getToken());
  }

  @Test
  void loginWithUnconfirmedEmailShouldThrowException() {
    testUser.setEmailConfirmation(false);
    when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(testUser);

    assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
  }

  @Test
  void loginWithInvalidCredentialsShouldThrowException() {
    when(userService.getUserByEmail(loginRequest.getEmail()))
      .thenThrow(new BadCredentialsException("Invalid credentials"));

    assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
  }

  @Test
  void registerSuccess() {
    User userToRegister = User.builder()
        .email("new@test.com")
        .password("password123")
        .role(User.Role.USER)
        .emailConfirmation(false)
        .build();

    Cart expectedCart = Cart.builder()
        .user(userToRegister)
        .build();

    when(userRepository.findByEmail(userToRegister.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
    when(userRepository.save(any())).thenReturn(userToRegister);
    when(cartRepository.save(any(Cart.class))).thenReturn(expectedCart);

    User registeredUser = authService.register(userToRegister);

    assertNotNull(registeredUser);
    assertNotNull(registeredUser.getConfirmationCode());
    assertFalse(registeredUser.isEmailConfirmation());
    verify(emailService).sendConfirmationCode(userToRegister);
    verify(userRepository).save(userToRegister);
    verify(cartRepository).save(any(Cart.class));
  }

  @Test
  void registerWithExistingEmailShouldThrowException() {
    User existingUser = User.builder()
      .email("existing@test.com")
      .build();

    when(userRepository.findByEmail(existingUser.getEmail()))
      .thenReturn(Optional.of(existingUser));

    assertThrows(BadRequestException.class, () -> authService.register(existingUser));
  }

  @Test
  void confirmEmailSuccess() {
    String code = "123456";
    testUser.setEmailConfirmation(false);
    testUser.setConfirmationCode(code);

    EmailConfirmationRequest request = EmailConfirmationRequest.builder()
        .email(testUser.getEmail())
        .confirmationCode(code)
        .password("password123")
        .build();

    UserDto testUserDto = UserDto.builder()
        .email(testUser.getEmail())
        .role(User.Role.USER)
        .build();

    when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
    when(jwtService.generateToken(testUser)).thenReturn("test.jwt.token");
    when(userMapper.entityToDto(testUser)).thenReturn(testUserDto);

    AuthResponse response = authService.confirmEmail(request);

    assertNotNull(response);
    assertNotNull(response.getToken());
    assertTrue(testUser.isEmailConfirmation());
    assertNull(testUser.getConfirmationCode());
    verify(userRepository).save(testUser);
  }

  @Test
  void confirmEmailWithInvalidCodeShouldThrowException() {
    testUser.setEmailConfirmation(false);
    testUser.setConfirmationCode("123456");

    EmailConfirmationRequest request = EmailConfirmationRequest.builder()
        .email(testUser.getEmail())
        .confirmationCode("invalid")
        .password("password123")
        .build();

    when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

    assertThrows(BadCredentialsException.class,
        () -> authService.confirmEmail(request));
  }

  @Test
  void changePasswordSuccess() {
    ChangePasswordRequest request = new ChangePasswordRequest(
      "oldPass", "newPass", "newPass"
    );

    when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
    when(passwordEncoder.matches(request.getCurrentPassword(), testUser.getPassword()))
      .thenReturn(true);

    authService.changePassword(testUser.getEmail(), request);

    verify(passwordEncoder).encode(request.getNewPassword());
    verify(userRepository).save(testUser);
  }

  @Test
  void changePasswordWithIncorrectCurrentPasswordShouldThrowException() {
    ChangePasswordRequest request = new ChangePasswordRequest(
      "wrongPass", "newPass", "newPass"
    );

    when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
    when(passwordEncoder.matches(request.getCurrentPassword(), testUser.getPassword()))
      .thenReturn(false);

    assertThrows(BadCredentialsException.class,
      () -> authService.changePassword(testUser.getEmail(), request));
  }
}
