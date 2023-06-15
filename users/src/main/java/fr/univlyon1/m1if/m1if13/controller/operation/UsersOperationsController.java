package fr.univlyon1.m1if.m1if13.controller.operation;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import fr.univlyon1.m1if.m1if13.dto.model.user.UserLoginDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserLogoutDto;
import fr.univlyon1.m1if.m1if13.exeption.EmptyParamException;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@Controller
public class UsersOperationsController {

  @Autowired
  private UserServiceImpl userService;

  @Operation(summary = "Logs user into the system", tags = { "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "successful operation", content = @Content),
      @ApiResponse(responseCode = "401", description = "Invalid password supplied", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
  @CrossOrigin(origins = {
      "http://localhost:8080",
      "http://localhost:3000",
      "http://localhost:3003",
      "http://localhost:3376",
      "http://51.38.178.218",
      "https://51.38.178.218"
  }, exposedHeaders = "Authorization")
  @PostMapping(path = "/login", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Void> login(
      @Parameter(description = "The user object that map the request payload", required = true)
      @RequestBody final UserLoginDto user,
      @Parameter(description = "Origin of the request", required = true)
      @RequestHeader("Origin") final String origin)
      throws AuthenticationException {
    return loginHandler(user, origin);
  }

  @Operation(summary = "Logout user form the system", tags = { "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
  @CrossOrigin(origins = {      "http://localhost:8080",
                                "http://localhost:3000",
                                "http://localhost:3003",
                                "http://localhost:3376",
                                "http://51.38.178.218",
                                "https://51.38.178.218" })
  @PostMapping(path = "/logout", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE })
  public ResponseEntity<Void> logout(
      @Parameter(description = "The user object that map the request payload", required = true)
      final @RequestBody UserLogoutDto user,
      @Parameter(description = "Origin of the request", required = true)
      final @RequestHeader("Origin") String origin)
      throws AuthenticationException {
    return logoutHandler(user, origin);
  }

  @Operation(summary = "Logs user into the system", tags = { "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "successful operation", content = @Content),
      @ApiResponse(responseCode = "401", description = "Invalid password supplied", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
  @CrossOrigin(origins = {
      "http://localhost:8080",
      "http://localhost:3000",
      "http://localhost:3003",
      "http://localhost:3376",
      "http://51.38.178.218",
      "https://51.38.178.218"
  }, exposedHeaders = "Authorization")
  @PostMapping(path = "/login", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public ResponseEntity<Void> loginForm(
      @Parameter(description = "The user object that map the request payload", required = true)
      @ModelAttribute("UserFormLoginData") final UserLoginDto user,
      @Parameter(description = "Origin of the request", required = true)
      final @RequestHeader("Origin") String origin)
      throws AuthenticationException {
    return loginHandler(user, origin);
  }

  @Operation(summary = "Logout user form the system", tags = { "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
  @CrossOrigin(origins = {       "http://localhost:8080",
                                 "http://localhost:3000",
                                 "http://localhost:3003",
                                 "http://localhost:3376",
                                 "http://51.38.178.218",
                                 "https://51.38.178.218" })
  @PostMapping(path = "/logout", consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE
  })
  public ResponseEntity<Void> logoutForm(
      @Parameter(description = "The user object that map the request payload", required = true)
      @ModelAttribute("UserFormLogoutData") final UserLogoutDto user,
      @Parameter(description = "Origin of the request", required = true)
      final @RequestHeader("Origin") String origin)
      throws AuthenticationException {
    return logoutHandler(user, origin);
  }

  @Operation(summary = "Check if JWT token has not expired and correspond to valid user", tags = { "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "Missing parameters", content = @Content),
      @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content)})
  @GetMapping("/authenticate")
  public ResponseEntity<Void> authenticate(
      final @RequestParam("jwt") String jwt,
      final @RequestParam("Origin") String origin) {
    try {
      String userLogin = userService.authenticate(jwt, origin);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set("X-Space-Chicken-Login", userLogin); // define a cutstom http header.
      return ResponseEntity.noContent().headers(responseHeaders).build();
    } catch (EmptyParamException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Missing parameters", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "Invalid Token", e);
    }
  }

  private ResponseEntity<Void> loginHandler(final UserLoginDto user, final String origin) {
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

  private ResponseEntity<Void> logoutHandler(final UserLogoutDto user, final String origin) {
    try {
      userService.logout(user, origin);
      return ResponseEntity.noContent().build();
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User not found", e);
    }
  }
}
