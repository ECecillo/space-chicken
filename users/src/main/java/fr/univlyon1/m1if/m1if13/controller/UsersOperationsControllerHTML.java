package fr.univlyon1.m1if.m1if13.controller;

import fr.univlyon1.m1if.m1if13.exeption.EmptyParamException;
import fr.univlyon1.m1if.m1if13.model.User;
import fr.univlyon1.m1if.m1if13.repository.dao.UserDao;
import fr.univlyon1.m1if.m1if13.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;

import fr.univlyon1.m1if.m1if13.exeption.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Controller
@RequestMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public class UsersOperationsControllerHTML {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenManager tokenManager;

    /**
     * Procédure de login utilisée par un utilisateur
     *
     * @param login    Le login de l'utilisateur. L'utilisateur doit avoir été créé
     *                 préalablement et son login doit être présent dans le DAO.
     * @param password Le password à vérifier.
     * @return Une ResponseEntity avec le JWT dans le header "Authentication" si le
     *         login s'est bien passé, et le code de statut approprié (204, 401 ou
     *         404).
     */
    @PostMapping(path = "/login")
    public ResponseEntity<Void> login(@RequestParam("login") String login, @RequestParam("password") String password,
            @RequestHeader("Origin") String origin) throws AuthenticationException {
        Optional<User> optionalUser = userDao.get(login);
        try {
            if (optionalUser.isPresent()) {
                optionalUser.get().authenticate(password);
                String token = tokenManager.generateToken(login);

                return ResponseEntity.noContent().header("Authorization", "Bearer " + token).build();
            } else {
                throw new UserNotFoundException();
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Wrong password", e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not Found", e);
        }
    }

    /**
     * Procédure de login utilisée par un utilisateur
     *
     * @param login Le login de l'utilisateur. L'utilisateur doit avoir été créé
     *              préalablement et son login doit être présent dans le DAO.
     * @return Une ResponseEntity avec le JWT dans le header "Authentication" si le
     *         login s'est bien passé, et le code de statut approprié (204, 401 ou
     *         404).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam("login") String login,
            @RequestHeader("Origin") String origin) throws AuthenticationException {
        Optional<User> optionalUser = userDao.get(login);
        try {
            if (optionalUser.isPresent()) {
                optionalUser.get().disconnect();
                return ResponseEntity.noContent().build();
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not founc", e);
        }
    }
    /**
     * Méthode destinée au serveur Node pour valider l'authentification d'un
     * utilisateur.
     *
     * @param token  Le token JWT qui se trouve dans le header "Authentication" de
     *               la requête
     * @param origin L'origine de la requête (pour la comparer avec celle du client,
     *               stockée dans le token JWT)
     * @return Une réponse vide avec un code de statut approprié (204, 400, 401).
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Void> authenticate(@RequestParam("jwt") String jwt, @RequestParam("origin") String origin) {
        try {
            if (jwt == null && origin == null) {
                throw new EmptyParamException();
            }
            String login = tokenManager.verifyToken(jwt);
            Optional<User> optionalUser = userDao.get(login);

            if (optionalUser.isPresent()) {
                return ResponseEntity.noContent().build();
            } else {
                throw new UserNotFoundException();
            }

        } catch (EmptyParamException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Missing parameters", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid Tokken", e);
        }
    }



}
