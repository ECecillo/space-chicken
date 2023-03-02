package fr.univlyon1.m1if.m1if13.users;

import javax.naming.AuthenticationException;

import fr.univlyon1.m1if.m1if13.model.Species;
import fr.univlyon1.m1if.m1if13.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

class UserTest {
    User jeanette, averell;

    @BeforeEach
    void setUp() {
        jeanette = new User("j2045", Species.POULE, "cotcodec");
        averell = new User("Averell Dalton", Species.COWBOY, "pan");
    }

    @Test
    void getLogin() {
        assert(jeanette.getLogin().equals("j2045"));
        assert(averell.getLogin().equals("Averell Dalton"));
    }

    @Test
    void getSpecies() {
        assert(jeanette.getSpecies().equals(Species.POULE));
        assert(averell.getSpecies().equals(Species.COWBOY));
    }

    @Test
    void setPassword() {
        jeanette.setPassword("codectoutcourt");
        try {
            jeanette.authenticate("codectoutcourt");
            assert(true);
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void isConnected() {
        try {
            jeanette.authenticate("cotcodec");
            assert(jeanette.isConnected());
            jeanette.disconnect();
            assert(!jeanette.isConnected());
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void authenticate() {
        try {
            jeanette.authenticate("cotcodec");
            assert(true);
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }

        try {
            averell.authenticate("cotcodec");
            fail("Mot de passe incorrect");
        } catch (AuthenticationException e) {
            assert(true);
        }
    }

    @Test
    void disconnect() {
        try {
            jeanette.authenticate("cotcodec");
            jeanette.disconnect();
            assert(!jeanette.isConnected());
        } catch (AuthenticationException e) {
            fail(e.getMessage());
        }
    }
}