package ru.hh.techradar.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import ru.hh.techradar.dto.UserDto;
import ru.hh.techradar.entity.User;

@Component
public class UserMapper implements BaseMapper<User, UserDto> {
  @Override
  public User toEntity(UserDto dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setPassword(dto.getPassword());
    return user;
  }

  @Override
  public UserDto toDto(User entity) {
    UserDto userDto = new UserDto();
    userDto.setId(entity.getId());
    userDto.setUsername(entity.getUsername());
    return userDto;
  }

  @Override
  public List<UserDto> toDtos(Collection<User> entities) {
    return entities.stream().map(this::toDto).toList();
  }

  @Override
  public List<User> toEntities(Collection<UserDto> dtos) {
    return dtos.stream().map(this::toEntity).toList();
  }

  public User toUpdate(User target, User source) {
    if (Objects.nonNull(source.getUsername())) {
      target.setUsername(source.getUsername());
    }
    return target;
  }
}
