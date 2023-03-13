package fr.univlyon1.m1if.m1if13.service;

import java.util.Optional;
import java.util.Set;

import javax.naming.AuthenticationException;
import javax.naming.NameAlreadyBoundException;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import fr.univlyon1.m1if.m1if13.dto.mapper.UserMapper;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserCreateDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserLoginDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserLogoutDto;
import fr.univlyon1.m1if.m1if13.exeption.EmptyParamException;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;
import fr.univlyon1.m1if.m1if13.utils.TokenManager;

@Component
public class UserServiceImpl implements UserServiceInterface {

  @Autowired
  private UserDao userDao;

  @Autowired
  private TokenManager tokenManager;

  @Autowired
  private UserMapper userMapper;

  @Override
  public Set<String> getUsers() {
    return userDao.getAll();
  }

  @Override
  public UserDto getUserByLogin(final String login) throws UserNotFoundException {
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isPresent()) {
      return userMapper.convertToDto(optionalUser.get());
    }
    throw new UserNotFoundException();
  }

  @Override
  public void signup(final UserCreateDto user) throws NameAlreadyBoundException {
    User newUser = new User(
      user.getLogin(),
      user.getPassword(),
      user.getSpecies(),
      user.getImage());
    userDao.save(newUser);
  }

  @Override
  public String login(final UserLoginDto user, final String origin)
    throws UserNotFoundException, AuthenticationException {
    final String login = user.getLogin();
    final String password = user.getPassword();
    Optional<User> optionalUser = userDao.get(login);

    if (optionalUser.isPresent()) {
      optionalUser.get().authenticate(password);
      String token = tokenManager.generateToken(login, origin);
      return "Bearer " + token;
    }
    throw new UserNotFoundException();
  }

  @Override
  public void logout(final UserLogoutDto user, final String origin)
    throws UserNotFoundException {
    String login = user.getLogin();
    Optional<User> optionalUser = userDao.get(login);
    try {
      if (optionalUser.isPresent()) {
        optionalUser.get().disconnect();
      } else {
        throw new UserNotFoundException();
      }
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User not found", e);
    }
  }

  @Override
  public void changePassword(final String login, final UserPasswordDto userDto) throws UserNotFoundException {
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      String[] params = {login, userDto.getPassword() };
      userDao.update(user, params);
    } else {
      throw new UserNotFoundException();
    }
  }

  @Override
  public void authenticate(final String jwt, final String origin) throws UserNotFoundException, EmptyParamException {
    if (jwt == null && origin == null) {
      throw new EmptyParamException();
    }
    String login = tokenManager.verifyToken(jwt);
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException();
    }
  }
}
