package fr.univlyon1.m1if.m1if13.dto.model.user;

public class UserInfoDto {

    private String password;
    private String image;

    /**
     * @param password User's password (in param or in URL).
     */
    public UserInfoDto(final String password, final String image) {
        this.password = password;
        this.image = image;
    }

    // Had to for Jackson.
    public UserInfoDto() {
        super();
    }

    /**
     * @return User's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return User's image.
     */
    public String getImage() {
        return image;
    }

    /**
     * @param password Set User's password in request.Set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @param image Set User's password in request.Set
     */
    public void setImage(final String image) {
        this.image = image;
    }
}
