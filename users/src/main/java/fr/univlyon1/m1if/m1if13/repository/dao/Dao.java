package fr.univlyon1.m1if.m1if13.repository.dao;

import java.util.Optional;
import java.util.Set;

import javax.naming.NameAlreadyBoundException;

public interface Dao<T> {
    /**
     * Get a regestered user.
     * @param id User's login.
     * @return java.util.Optional of the user (empty if not found).
     */
    Optional<T> get(String id);

    /**
     * Get all registered users.
     * @return set of all registered user's logins.
     */
    Set<String> getAll();

    /**
     * Create a new user and register it.
     * @param t user to register.
     * @throws NameAlreadyBoundException
     */
    void save(T t) throws NameAlreadyBoundException;

    /**
     * Modify a registered user.
     * @param t User to modify.
     * @param params Array of 2 Strings : login and password
     */
    void update(T t, String[] params);

    /**
     * Delete a registered user.
     * @param t User to delete.
     */
    void delete(T t);
}
