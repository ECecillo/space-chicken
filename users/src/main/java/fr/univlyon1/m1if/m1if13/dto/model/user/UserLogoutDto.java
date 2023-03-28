package fr.univlyon1.m1if.m1if13.dto.model.user;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import nonapi.io.github.classgraph.json.Id;

@EntityScan
public class UserLogoutDto {

  @Id
  private String login;

  /**
   * @param login User's login (in param or in URL).
   */
  public UserLogoutDto(final String login) {
    this.login = login;
  }

  // Had to for Jackson.
  public UserLogoutDto() {
    super();
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
}
