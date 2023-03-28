package fr.univlyon1.m1if.m1if13.dto.model.user;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import nonapi.io.github.classgraph.json.Id;

@EntityScan
public class UserLoginDto {

  @Id
  private String login;
  private String password;

  /**
   * @param login    User's login (in param or in URL).
   * @param password User's password.
   */
  public UserLoginDto(final String login, final String password) {
    this.login = login;
    this.password = password;
  }

  // Had to for Jackson.
  public UserLoginDto() {
    this.login = "";
    this.password = "";
  }

  /**
   * @return User's login.
   */
  public String getLogin() {
    return login;
  }

  /**
   * @param login Set User's login in request.Set
   */
  public void setLogin(final String login) {
    this.login = login;
  }

  /**
   * @param password Set User's password in request.
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * @return User's password.
   */
  public String getPassword() {
    return password;
  }
}
