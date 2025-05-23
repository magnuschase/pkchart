package org.magnuschase.pkchart.service;

import org.magnuschase.pkchart.entity.User;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.model.Role;
import org.magnuschase.pkchart.repository.UserRepository;
import org.magnuschase.pkchart.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtUtil jwtUtil;

  public LoginResponseDTO login(LoginRequestDTO body) {
    String email = body.getEmail();
    String password = body.getPassword();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
    String token = jwtUtil.generateToken(email, user.getRole().name());
    LoginResponseDTO response = new LoginResponseDTO();
    response.setAccessToken(token);
    return response;
  }

  public LoginResponseDTO register(RegisterRequestDTO body) {
    User user = new User();
    user.setEmail(body.getEmail());
    user.setUsername(body.getUsername());
    user.setPassword(passwordEncoder.encode(body.getPassword()));
    user.setDisplayName(body.getDisplayName());
    user.setRole(Role.USER);
    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    LoginResponseDTO response = new LoginResponseDTO();
    response.setAccessToken(token);
    return response;
  }

  public User promoteUserRole(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if (user.getRole() == Role.ADMIN) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already an admin");
    }
    user.setRole(Role.ADMIN);

    return userRepository.save(user);
  }

  public UserDetailsDTO getAccountInfo(UserDetails userDetails) {
    User user =
        userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    UserDetailsDTO dto = new UserDetailsDTO();
    dto.setEmail(user.getEmail());
    dto.setUsername(user.getUsername());
    dto.setDisplayName(user.getDisplayName());
    return dto;
  }

  public UserDetailsDTO updateAccountInfo(UserDetails userDetails, UpdateAccountDTO updateDto) {
    User user =
        userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if (updateDto.getDisplayName() != null && !updateDto.getDisplayName().trim().isEmpty())
      user.setDisplayName(updateDto.getDisplayName());
    if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty())
      user.setEmail(updateDto.getEmail());
    User newUser = userRepository.save(user);
    UserDetailsDTO dto = new UserDetailsDTO();
    dto.setEmail(newUser.getEmail());
    dto.setUsername(newUser.getUsername());
    dto.setDisplayName(newUser.getDisplayName());
    return dto;
  }

  public void changePassword(UserDetails userDetails, ChangePasswordDTO dto) {
    User user =
        userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
    }
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    userRepository.save(user);
  }
}
