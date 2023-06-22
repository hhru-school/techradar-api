package ru.hh.techradar.security.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.enumeration.Role;
import ru.hh.techradar.security.dto.AuthenticationDto;
import ru.hh.techradar.security.dto.AuthenticationResponse;
import static ru.hh.techradar.security.dto.Constants.AUTHORIZATION;
import static ru.hh.techradar.security.dto.Constants.TOKEN_TYPE_LENGTH;
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
      user.setRole(Role.MEMBER);
      //todo нужно проработать логику привязки пользователя к компании
      userService.save(user);
      return Map.of("message", "Успешная регистрация!");
    }
    throw new IllegalArgumentException("Подтвержденный пароль не совпадает с паролем!");
  }

  public AuthenticationResponse authenticate(AuthenticationDto auth) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
    User user = userService
        .findByUsername(auth.getUsername());
    CustomUserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
    return new AuthenticationResponse(
        tokenService.generateAccessToken(userDetails),
        tokenService.generateRefreshToken(userDetails));
  }

  public AuthenticationResponse refresh(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION);
    String refreshToken = authHeader.substring(TOKEN_TYPE_LENGTH);
    String username = tokenService.extractUsername(refreshToken);
    User user = userService
        .findByUsername(username);
    UserDetails userDetails = CustomUserDetails.toCustomUserDetails(user);
    return new AuthenticationResponse(
        tokenService.generateAccessToken(userDetails),
        refreshToken);
  }
}
