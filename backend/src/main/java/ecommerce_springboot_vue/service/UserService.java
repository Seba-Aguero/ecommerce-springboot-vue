package ecommerce_springboot_vue.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ecommerce_springboot_vue.dto.UserDto;
import ecommerce_springboot_vue.dto.request.user.UpdateProfileRequest;
import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.exception.BadRequestException;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.mapper.UserMapper;
import ecommerce_springboot_vue.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
  private final IUserRepository userRepository;
  private final UserMapper userMapper;

  public UserDto getCurrentUserProfile() {
    User user = getCurrentUser();
    return userMapper.entityToDto(user);
  }

  public UserDto updateProfile(UpdateProfileRequest request) {
    User currentUser = getCurrentUser();

    // Check if email is already taken by another user
    if (!request.getEmail().equals(currentUser.getEmail())) {
      boolean emailExists = userRepository.findByEmail(request.getEmail()).isPresent();
      if (emailExists) {
        throw new BadRequestException("Email already taken");
      }
    }

    currentUser.setFirstName(request.getFirstName());
    currentUser.setLastName(request.getLastName());
    currentUser.setEmail(request.getEmail());
    currentUser.setPhone(request.getPhone());
    currentUser.setAddress(request.getAddress());

    currentUser = userRepository.save(currentUser);
    return userMapper.entityToDto(currentUser);
  }

  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public User getUserByEmail(String email){
    return userRepository.findByEmail(email)
      .orElseThrow(()-> new ResourceNotFoundException("User not found"));
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }
}
