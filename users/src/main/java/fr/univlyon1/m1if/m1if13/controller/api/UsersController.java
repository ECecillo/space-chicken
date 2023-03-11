package fr.univlyon1.m1if.m1if13.controller.api;

import java.util.Set;

import javax.naming.NameAlreadyBoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import fr.univlyon1.m1if.m1if13.dto.mapper.UserMapper;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.service.UserServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {
  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserServiceImpl userService;

  @PostMapping(path = "/users", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<?> createUser(final @RequestBody UserDto user) {
    return signupHandler(user);
  }

  @PutMapping(path = "/users/{login}", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<?> updateUserPassword(
      final @PathVariable String login,
      final @RequestBody UserDto userDto) {
    return passwordUpdateHandler(login, userDto);
  }

  @PostMapping(path = "/users", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<?> createUserForm(final UserDto user) {
    return signupHandler(user);
  }

  @PutMapping(path = "/users/{login}", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<?> updateUserPasswordForm(
      final @PathVariable String login,
      final UserDto userDto) {
    return passwordUpdateHandler(login, userDto);
  }

  @GetMapping(path = "/users")
  public ResponseEntity<Set<String>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"})
  @GetMapping(path = "/users/{login}")
  public ResponseEntity<User> getUser(final @PathVariable String login) {
    try {
      return ResponseEntity.ok(userMapper.convertToEntity(userService.getUserByLogin(login)));
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "l'utilisateur n'a pas été trouvé");
    }
  }

  private ResponseEntity<?> signupHandler(final UserDto user) {
    try {
      userService.signup(user);
      return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    } catch (NameAlreadyBoundException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "User already exist.", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create User", e);
    }
  }

  private ResponseEntity<?> passwordUpdateHandler(final String login, final UserDto userDto) {
    try {
      userService.changePassword(login, userDto);
      return new ResponseEntity<>("Password updated successfully", HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found");
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Password", e);
    }
  }
}
