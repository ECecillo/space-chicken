package fr.univlyon1.m1if.m1if13.users.unit;

import javax.naming.AuthenticationException;

import fr.univlyon1.m1if.m1if13.model.Species;
import fr.univlyon1.m1if.m1if13.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

/**
 * Test class for the User class.
 */
class UserTest {
    private User jeanette;
    private User averell;

    /**
     * Initialize the test class.
     */
    @BeforeEach
    void setUp() {
        jeanette = new User("j2045", "cotcodec", Species.POULE);
        averell = new User("Averell Dalton", "pan", Species.COWBOY);
    }

    /**
     * Test the getLogin method.
     */
    @Test
    void getLogin() {
        assert (jeanette.getLogin().equals("j2045"));
        assert (averell.getLogin().equals("Averell Dalton"));
    }

    /**
     * Test the getSpecies method.
     */
    @Test
    void getSpecies() {
        assert (jeanette.getSpecies().equals(Species.POULE));
        assert (averell.getSpecies().equals(Species.COWBOY));
    }

    /**
     * Test the getPassword method.
     */
    @Test
    void setPassword() {
        jeanette.setPassword("codectoutcourt");
        try {
            jeanette.authenticate("codectoutcourt");
            assert (true);
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test the isConnected method.
     */
    @Test
    void isConnected() {
        try {
            jeanette.authenticate("cotcodec");
            assert (jeanette.isConnected());
            jeanette.disconnect();
            assert (!jeanette.isConnected());
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test the authenticate method.
     */
    @Test
    void authenticate() {
        try {
            jeanette.authenticate("cotcodec");
            assert (true);
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }

        try {
            averell.authenticate("cotcodec");
            fail("Mot de passe incorrect");
        } catch (AuthenticationException e) {
            assert (true);
        }
    }

    /**
     * Test the disconnect method.
     */
    @Test
    void disconnect() {
        try {
            jeanette.authenticate("cotcodec");
            jeanette.disconnect();
            assert (!jeanette.isConnected());
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }
}
