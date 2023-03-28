package fr.univlyon1.m1if.m1if13.service;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;

/**
 * A small service that handle sensitive requests.
 */
public interface AdminServiceInterface {

  /**
   *
   * Remove a user from application ressource.
   *
   * @param login user to remove.
   * @throws UserNotFoundException if we could not find the user with the mapped.
   */
  void deleteUser(String login) throws UserNotFoundException;
}
