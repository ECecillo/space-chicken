package fr.univlyon1.m1if.m1if13.model;

import javax.naming.AuthenticationException;

public class User {
    private final String login;
    private final Species species;
    private String password;
    private boolean connected = false;
    private String image;

    public User(final String login, final Species species, final String password) {
        this.login = login;
        this.species = species;
        this.password = password;
    }

    public User(final String login, final Species species, final String password, final String image) {
        this.login = login;
        this.species = species;
        this.password = password;
        this.image = image != null ? image : "";
    }

    // Had to for Jackson.
    public User() {
        this.login = "";
        this.species = Species.POULE;
        this.password = "";
        this.image = "";
    }

    public String getLogin() {
        return login;
    }

    public Species getSpecies() {
        return species;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void authenticate(final String password) throws AuthenticationException {
        if (!password.equals(this.password)) {
            throw new AuthenticationException("Error wrong password");
        }
        this.connected = true;
    }

    public void disconnect() {
        this.connected = false;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }
}
