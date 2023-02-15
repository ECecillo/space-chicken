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
   * @param login    Le login de l'utilisateur (peut se trouver dans un paramètre
   *                 ou dans l'URL)
   * @param password Le password (dans un paramètre)
   * @param name     Le nom (dans un paramètre)
   */
  public UserDto(String login, String password, Species species, String image) {
    this.login = login;
    this.password = password;
    this.species = species;
    this.image = image;
  }

  /**
   * @param login Le login de l'utilisateur présent dans la requête
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * @param password Le password de l'utilisateur présent dans la requête
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @param name Le nom de l'utilisateur présent dans la requête
   */
  public void setSpecies(Species species) {
    this.species = species;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public void setImage(String image) {
    this.image = image;
  }

  /**
   * @return Le login de l'utilisateur passé dans la requête
   */
  public String getLogin() {
    return login;
  }

  /**
   * @return Le password de l'utilisateur passé dans la requête
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return L'espère de l'utilisateur passé dans la requête
   */
  public Species getSpecies() {
    return species;
  }

  public Boolean getConnected() {
    return connected;
  }

  public String getImage() {
    return image;
  }
}
