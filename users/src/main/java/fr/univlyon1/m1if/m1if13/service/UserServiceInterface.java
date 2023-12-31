package fr.univlyon1.m1if.m1if13.service;

import java.util.Set;

import javax.naming.AuthenticationException;
import javax.naming.NameAlreadyBoundException;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserCreateDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserLoginDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserLogoutDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserInfoDto;
import fr.univlyon1.m1if.m1if13.dto.model.user.UserPasswordDto;
import fr.univlyon1.m1if.m1if13.exeption.EmptyParamException;
import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;

/**
 * Services that handle user requests.
 */
public interface UserServiceInterface {

  /**
   * Find and return user informations.
   *
   * @param login
   * @return DTO with all the user informations stored in the DAO.
   */
  UserDto getUserByLogin(String login) throws UserNotFoundException;

  /**
   * @return A list of all the user login stored in the ressource handler.
   */
  Set<String> getUsers();

  /**
   * Create a new user inside our user DAO.
   *
   * @param user mapped object with login and password of the user.
   * @throws NameAlreadyBoundException if user already exist.
   */
  void signup(UserCreateDto user) throws NameAlreadyBoundException;

  /**
   * Procedure for login a user.
   *
   * @param user mapped object with login and password of the user. The user must
   *             have been created previously and his login must be present in the
   *             DAO.
   * @throws UserNotFoundException   if user does not exist.
   * @throws AuthenticationException if we were not able to authenticate the use
   *                                 with his password.
   * @return String with "Bearer JWT" format if :
   *         login's passed with appropriate status code :
   *         201 created,
   *         401 unauthorized,
   *         404 not found
   */
  String login(UserLoginDto user, String origin) throws UserNotFoundException, AuthenticationException;

  /**
   * Turn isConnected state to false.
   *
   * @param user   mapped object containing user login
   * @param origin The origin of the request (compared
   *               with origin of the client sotred in JWT Token)
   * @throws UserNotFoundException if we could not find the user with the mapped
   *                               payload.
   */
  void logout(UserLogoutDto user, String origin) throws UserNotFoundException;

  /**
   * Update the user password.
   *
   * @param login   user login.
   * @param userDto Mapped user parameters passed in the query.
   * @throws UserNotFoundException if we could not find the user with the mapped
   *                               payload.
   */
  void changePassword(String login, UserPasswordDto userDto) throws UserNotFoundException;

  /**
   * Update the user password.
   *
   * @param login   user login.
   * @param userDto Mapped user parameters passed in the query.
   * @throws UserNotFoundException if we could not find the user with the mapped
   *                               payload.
   */
  void changeInfos(String login, UserInfoDto userDto) throws UserNotFoundException;

  /**
   * Check if the JWT is valid and correspond to a user in the DAO.
   *
   * @param jwt    token.
   * @param origin request origin, for comparison with the origin stored in the
   *               JWT token.
   * @return The user login corresponding to the JWT token.
   * @throws UserNotFoundException if we could not find the user with the mapped
   *                               object.
   * @throws EmptyParamException   if the origin and jwt have not been passed.
   */
  String authenticate(String jwt, String origin) throws UserNotFoundException, EmptyParamException;
}
