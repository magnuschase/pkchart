package org.magnuschase.pkchart.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.magnuschase.pkchart.interfaces.*;
import org.magnuschase.pkchart.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "Authentication and user management")
public class AuthController {

  @Autowired private AuthService authService;

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody LoginRequestDTO body) {
    return authService.login(body);
  }

  @PostMapping("/register")
  public LoginResponseDTO register(@RequestBody RegisterRequestDTO body) {
    return authService.register(body);
  }

  @GetMapping("/user-details")
  public UserDetailsDTO getAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {
    return authService.getAccountInfo(userDetails);
  }

  @PutMapping("/user-details")
  public UserDetailsDTO updateAccountInfo(
      @AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateAccountDTO updateDto) {
    return authService.updateAccountInfo(userDetails, updateDto);
  }

  @PostMapping("/change-password")
  public void changePassword(
      @AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordDTO dto) {
    authService.changePassword(userDetails, dto);
  }
}
