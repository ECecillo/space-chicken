package fr.univlyon1.m1if.m1if13.dto.model.user;

import nonapi.io.github.classgraph.json.Id;

public class UserPasswordDto {

    @Id
    private String password;

    /**
     * @param password User's password (in param or in URL).
     */
    public UserPasswordDto(final String password) {
        this.password = password;
    }

    // Had to for Jackson.
    public UserPasswordDto() {
        super();
    }

    /**
     * @return User's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password Set User's password in request.Set
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
