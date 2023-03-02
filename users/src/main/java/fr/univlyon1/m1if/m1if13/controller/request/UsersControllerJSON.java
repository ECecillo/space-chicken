package fr.univlyon1.m1if.m1if13.controller.request;

import java.util.Optional;

import javax.naming.NameAlreadyBoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fr.univlyon1.m1if.m1if13.dto.mapper.UserMapper;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
public class UsersControllerJSON {
  @Autowired
  private UserDao userDao;

  @Autowired
  private UserMapper userMapper;

  @PostMapping("/users")
  public ResponseEntity<?> createUser(@RequestBody UserDto user) {
    try {
      User newUser = userMapper.convertToEntity(user);
      userDao.save(newUser);
      return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    } catch (NameAlreadyBoundException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "User already exist.", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "FAILED to create User", e);
    }
  }

  @PutMapping("/users/{login}")
  public ResponseEntity<?> updateUserPassword(@PathVariable String login, @RequestBody UserDto userDto) {
    try {
      Optional<User> optionalUser = userDao.get(login);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        String[] params = { login, userDto.getPassword() };
        userDao.update(user, params);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.NO_CONTENT);
      } else {
        throw new UserNotFoundException();
      }
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found");
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Password", e);
    }
  }
}
