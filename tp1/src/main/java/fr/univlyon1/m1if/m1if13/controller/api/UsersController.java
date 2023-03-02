package fr.univlyon1.m1if.m1if13.controller.api;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {

  @Autowired
  private UserDao userDao;

  @GetMapping("/users")
  public ResponseEntity<Set<String>> getUsers() {
    Set<String> users = userDao.getAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping(path = "/users/{login}", produces = { MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_HTML_VALUE })
  public ResponseEntity<User> getUser(@PathVariable String login) {
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isPresent()) {
      return ResponseEntity.ok(optionalUser.get());
    } else {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "l'utilisateur n'a pas été trouvé");
    }
  }

  @DeleteMapping("/users/{login}")
  public ResponseEntity<?> deleteUser(@PathVariable String login) {
    try {
      Optional<User> optionalUser = userDao.get(login);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        userDao.delete(user);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
      } else {
        throw new UserNotFoundException();
      }
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found", e);

    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete Password", e);
    }
  }
}
