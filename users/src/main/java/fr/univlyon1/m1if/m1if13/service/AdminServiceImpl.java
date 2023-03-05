package fr.univlyon1.m1if.m1if13.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;

@Component
public class AdminServiceImpl implements AdminServiceInterface {
  @Autowired
  private UserDao userDao;

  @Override
  public void deleteUser(final String login) throws UserNotFoundException {
    Optional<User> optionalUser = userDao.get(login);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      userDao.delete(user);
    } else {
      throw new UserNotFoundException();
    }
  }

}
