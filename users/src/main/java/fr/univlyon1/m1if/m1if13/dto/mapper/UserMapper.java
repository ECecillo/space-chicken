package fr.univlyon1.m1if.m1if13.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.model.User;

@Component
public class UserMapper {

  @Autowired
  private ModelMapper modelMapper;

  public UserDto convertToDto(final User user) {
    return modelMapper.map(user, UserDto.class);
  }

  public User convertToEntity(final UserDto userDto) throws ParseException {
    return modelMapper.map(userDto, User.class);
  }
}
