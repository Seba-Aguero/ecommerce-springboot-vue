package ecommerce_springboot_vue.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ecommerce_springboot_vue.entity.User;
import ecommerce_springboot_vue.exception.ResourceNotFoundException;
import ecommerce_springboot_vue.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
  private final IUserRepository userRepository;

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