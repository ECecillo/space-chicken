package fr.univlyon1.m1if.m1if13.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.service.AdminServiceImpl;

@RestController
public class AdminController {

  @Autowired
  private AdminServiceImpl adminService;

  @Operation(summary = "Delete user",
          description = "Delete the user with the login in parameter.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "user deteled", content = @Content),
          @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @DeleteMapping("/users/{login}")
  public ResponseEntity<?> deleteUser(final @PathVariable String login) {
    try {
      adminService.deleteUser(login);
      return new ResponseEntity<>("User deleted successfully", HttpStatus.NO_CONTENT);
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_IMPLEMENTED, "Failed to delete user", e);
    }
  }

}
