package ru.hh.techradar.security.model;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import javax.security.auth.Subject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hh.techradar.entity.User;
import ru.hh.techradar.enumeration.Role;

public class CustomUserDetails implements UserDetails, Principal {

  private Long id;
  private String username;
  private String password;
  private Role role;

  public CustomUserDetails() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static CustomUserDetails toCustomUserDetails(User user) {
    CustomUserDetails customUserDetails = new CustomUserDetails();
    customUserDetails.setId(user.getId());
    customUserDetails.setUsername(user.getUsername());
    customUserDetails.setPassword(user.getPassword());
    customUserDetails.setRole(user.getRole());
    return customUserDetails;
  }

  @Override
  public String getName() {
    return username;
  }

  @Override
  public boolean implies(Subject subject) {
    return Principal.super.implies(subject);
  }
}
