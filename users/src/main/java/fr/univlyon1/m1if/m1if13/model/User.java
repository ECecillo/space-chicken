package fr.univlyon1.m1if.m1if13.model;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.naming.AuthenticationException;

@EntityScan
public class User {

    @Id
    private final String login;
    private String password;
    private final Species species;
    private boolean connected = false;
    private String image;

    /**
     * @param login User's login (in param or in URL).
     * @param password User's password.
     * @param species User's species.
     */
    public User(final String login, final String password, final Species species) {
        this.login = login;
        this.password = password;
        this.species = species;
    }

    /**
     * @param login User's login (in param or in URL).
     * @param password User's password.
     * @param species User's species.
     * @param image User's image.
     */
    public User(final String login, final String password, final Species species, final String image) {
        this.login = login;
        this.password = password;
        this.species = species;
        this.image = image != null ? image : "";
    }


    // Had to for Jackson.
    public User() {
        this.login = "";
        this.password = "";
        this.species = Species.CHICKEN;
        this.image = "";
    }

    /**
     * @return User's login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return User's species.
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * @param password Set User's password in request.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return if the user is connected.
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * @param password compare the password with the user's password.
     */
    public void authenticate(final String password) throws AuthenticationException {
        if (!password.equals(this.password)) {
            throw new AuthenticationException("Error wrong password");
        }
        this.connected = true;
    }

    /**
     * Set connect status the user.
     */
    public void disconnect() {
        this.connected = false;
    }

    /**
     * @return User's image.
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image Set User's image in request.
     */
    public void setImage(final String image) {
        this.image = image;
    }
}
