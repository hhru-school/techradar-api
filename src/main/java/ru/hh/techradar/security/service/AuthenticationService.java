package ru.hh.techradar.security.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.enumeration.Role;
import ru.hh.techradar.exception.NotFoundException;
import ru.hh.techradar.security.dto.AuthenticationDto;
import ru.hh.techradar.security.dto.AuthenticationResponse;
import static ru.hh.techradar.security.dto.Constants.AUTHORIZATION;
import static ru.hh.techradar.security.dto.Constants.TOKEN_TYPE;
import ru.hh.techradar.security.dto.RegisterDto;
import ru.hh.techradar.security.model.CustomUserDetails;
import ru.hh.techradar.service.UserService;

@Service
public class AuthenticationService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationService(
      UserService userService,
      PasswordEncoder passwordEncoder,
      TokenService tokenService,
      AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
  }

  public Map<String, String> register(RegisterDto register) {
    if (register.getPassword().equals(register.getConfirmPassword())) {
      User user = new User();
      user.setUsername(register.getUsername());
      user.setPassword(passwordEncoder.encode(register.getPassword()));
      user.setRole(Role.USER);
      userService.save(1L, user);
      return Map.of("message", "Registration success!");
    }
    throw new IllegalArgumentException("Confirm password not equals password!");
  }

  public AuthenticationResponse authenticate(AuthenticationDto auth) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
    User user = userService
        .findByUsername(auth.getUsername())
        .orElseThrow(() -> new NotFoundException(User.class, auth.getUsername()));
    CustomUserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
    return new AuthenticationResponse(
        tokenService.generateAccessToken(userDetails),
        tokenService.generateRefreshToken(userDetails));
  }

  public AuthenticationResponse refresh(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION);
    if (Objects.nonNull(authHeader) && authHeader.startsWith(TOKEN_TYPE)) {
      String refreshToken = authHeader.substring(7);
      String username = tokenService.extractUsername(refreshToken);
      if (Objects.nonNull(username)) {
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        UserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
        if (tokenService.isTokenValid(refreshToken, userDetails)) {
          return new AuthenticationResponse(
              tokenService.generateAccessToken(userDetails),
              refreshToken);
        }
      }
    }
    throw new BadCredentialsException("Token not valid!");
  }
}
