package fr.univlyon1.m1if.m1if13.dto.model.user;

import fr.univlyon1.m1if.m1if13.model.Species;

public class UserDto {
  private String login;
  private String password;
  private Species species;
  private boolean connected = false;
  // Nom du fichier image qui représentera l'utilisateur sur la carte
  private String image;

  public UserDto() {
    super();
  }

  /**
   * @param login User's login (in param or in URL).
   * @param password User's password.
   * @param species User's species.
   * @param image User's image.
   */
  public UserDto(final String login, final String password, final Species species, final String image) {
    this.login = login;
    this.password = password;
    this.species = species;
    this.image = image;
  }

  /**
   * @param login Set User's login in request.
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
   * @param species Set User's species in request.
   */
  public void setSpecies(final Species species) {
    this.species = species;
  }

  /**
   * @param connected Set User's connected in request.
   */
  public void setConnected(final boolean connected) {
    this.connected = connected;
  }

  /**
   * @param image Set User's image in request.
   */
  public void setImage(final String image) {
    this.image = image;
  }

  /**
   * @return User's login in request.
   */
  public String getLogin() {
    return login;
  }

  /**
   * @return User's password in request.
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return User's species in request.
   */
  public Species getSpecies() {
    return species;
  }

    /**
     * @return User's connected in request.
     */
  public Boolean getConnected() {
    return connected;
  }

    /**
     * @return User's image in request.
     */
  public String getImage() {
    return image;
  }
}
