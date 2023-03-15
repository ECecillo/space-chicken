package fr.univlyon1.m1if.m1if13.controller.api;

import java.util.Set;

import javax.naming.NameAlreadyBoundException;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserPasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import fr.univlyon1.m1if.m1if13.dto.mapper.UserMapper;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserCreateDto;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.service.UserServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {
  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserServiceImpl userService;

  @Operation(summary = "Create user",
          description = "Create the user with the login in parameter.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "User created", content = @Content),
          @ApiResponse(responseCode = "409", description = "User already exist", content = @Content)
  })
  @PostMapping(path = "/users", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<?> createUser(
    @Parameter(description = "The user object that map the request payload", required = true)
    @RequestBody final UserCreateDto user) {
    return signupHandler(user);
  }

  @Operation(summary = "Update user",
          description = "Update user's password")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "User's password updated", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @Parameters({})
  @PutMapping(path = "/users/{login}", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<?> updateUserPassword(
      final @PathVariable String login,
      @Parameter(description = "The user object that map the request payload", required = true)
      final @RequestBody UserPasswordDto userDto) {
    return passwordUpdateHandler(login, userDto);
  }

  @Operation(summary = "Create user",
          description = "Create the user with the login in parameter .")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "User created", content = @Content),
          @ApiResponse(responseCode = "409", description = "User already exist", content = @Content)
  })
  @Parameters({})
  @PostMapping(path = "/users", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<?> createUserForm(
      @Parameter(description = "The user object that map the request payload", required = true)
      final UserCreateDto user) {
    return signupHandler(user);
  }

  @Operation(summary = "Update user",
          description = "Update user's password")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "User's password updated", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content)

  })
  @Parameters({})
  @PutMapping(path = "/users/{login}", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<?> updateUserPasswordForm(
      final @PathVariable String login,
      @Parameter(description = "The user object that map the request payload", required = true)
      final UserPasswordDto userDto) {
    return passwordUpdateHandler(login, userDto);
  }

  @Operation(summary = "Get users",
          description = "Get all Users logins")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "get users successfully"),
          @ApiResponse(responseCode = "404", description = "Users not found", content = @Content)

  })
  @GetMapping(path = "/users")
  public ResponseEntity<Set<String>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @Operation(summary = "Get user",
          description = "Get one user by searching with his login")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "get user successfully"),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content)

  })
  @CrossOrigin(origins = {"http://localhost:8080", "http://192.168.75.14", "https://192.168.75.14"})
  @GetMapping(path = "/users/{login}")
  public ResponseEntity<User> getUser(
  final @PathVariable String login) {
    try {
      return ResponseEntity.ok(userMapper.convertToEntity(userService.getUserByLogin(login)));
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "l'utilisateur n'a pas été trouvé");
    }
  }

  private ResponseEntity<?> signupHandler(final UserCreateDto user) {
    try {
      if (user.getSpecies() == null) {
        throw new Exception(" No species given");
      }
      userService.signup(user);
      return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    } catch (NameAlreadyBoundException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "User already exist.", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to create User", e);
    }
  }

  private ResponseEntity<?> passwordUpdateHandler(final String login, final UserPasswordDto userDto) {
    try {
      userService.changePassword(login, userDto);
      return new ResponseEntity<>("Password updated successfully", HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found");
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to update Password", e);
    }
  }
}
