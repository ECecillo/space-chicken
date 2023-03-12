package fr.univlyon1.m1if.m1if13.controller.operation;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.exeption.EmptyParamException;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.service.UserServiceImpl;

@Controller
public class UsersOperationsController {

  @Autowired
  private UserServiceImpl userService;

  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"},
          exposedHeaders = "Authorization")
  @PostMapping(path = "/login", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Void> login(
          final @RequestBody UserDto user,
          final @RequestHeader("Origin") String origin) throws AuthenticationException {
    return loginHandler(user, origin);
  }

  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"})
  @PostMapping(path = "/logout", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Void> logout(
          final @RequestBody UserDto user,
          final @RequestHeader("Origin") String origin) throws AuthenticationException {
    return logoutHandler(user, origin);
  }

  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"})
  @PostMapping(path = "/login", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<Void> loginForm(
          final @ModelAttribute("UserFormLoginData") UserDto user,
          final @RequestHeader("Origin") String origin) throws AuthenticationException {
    return loginHandler(user, origin);
  }

  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"})
  @PostMapping(path = "/logout", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE
  })
  public ResponseEntity<Void> logoutForm(
          final @ModelAttribute("UserFormLogoutData") UserDto user,
          final @RequestHeader("Origin") String origin) throws AuthenticationException {
    return logoutHandler(user, origin);
  }

  @GetMapping("/authenticate")
  public ResponseEntity<Void> authenticate(
          final @RequestParam("jwt") String jwt,
          final @RequestParam("Origin") String origin) {
    try {
      userService.authenticate(jwt, origin);
      return ResponseEntity.noContent().build();
    } catch (EmptyParamException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Missing parameters", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "Invalid Token", e);
    }
  }

  private ResponseEntity<Void> loginHandler(final UserDto user, final String origin) {
    try {
      final String token = userService.login(user, origin);
      return ResponseEntity.noContent().header("Authorization", token).build();
    } catch (AuthenticationException e) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "Wrong password", e);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User not Found", e);
    }
  }

  private ResponseEntity<Void> logoutHandler(final UserDto user, final String origin) {
    try {
      userService.logout(user, origin);
      return ResponseEntity.noContent().build();
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User not found", e);
    }
  }
}
