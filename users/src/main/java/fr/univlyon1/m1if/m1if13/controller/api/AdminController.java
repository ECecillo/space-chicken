package fr.univlyon1.m1if.m1if13.controller.api;

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
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete Password", e);
    }
  }

}
