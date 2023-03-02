package fr.univlyon1.m1if.m1if13.controller.request;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.univlyon1.m1if.m1if13.model.Species;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import javax.naming.NameAlreadyBoundException;

@RestController
@RequestMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public class UserControllerHTML {
  @Autowired
  private UserDao userDao;

  @PostMapping("/users")
  public ResponseEntity<?> createUser(@RequestParam String login, @RequestParam String password,
      @RequestParam Species species, @RequestParam(required = false) String images) {
    try {
      User user = new User(login, species, password, images);
      userDao.save(user);
      return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    } catch (NameAlreadyBoundException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "User already exist.", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "FAILED to create User", e);
    }
  }

  @GetMapping(path = "/users/{login}")
  public ResponseEntity<User> getUser(@PathVariable String login) {
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isPresent()) {
      return ResponseEntity.ok(optionalUser.get());
    } else {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "l'utilisateur n'a pas été trouvé");
    }
  }

  @PutMapping(path = "/users/{login}")
  public ResponseEntity<?> updateUserPassword(@PathVariable String login, @RequestParam String password) {
    try {
      Optional<User> optionalUser = userDao.get(login);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        String[] params = { login, password };
        userDao.update(user, params);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.NO_CONTENT);
      } else {
        throw new UserNotFoundException();
      }
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User Not Found", e);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Password", e);
    }
  }
}
