package fr.univlyon1.m1if.m1if13.repository.dao;

import fr.univlyon1.m1if.m1if13.model.Species;
import fr.univlyon1.m1if.m1if13.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.naming.NameAlreadyBoundException;

@Component
public class UserDao implements Dao<User> {

    private Map<String, User> users = new HashMap<>();

    public UserDao() {
        this.users.put("ECecillo", new User("ECecillo", "root", Species.POULE));
        this.users.put("admin", new User("admin", "root", Species.COWBOY));
        this.users.put("John", new User("John", "johnPassword", Species.COWBOY));
        this.users.put("Susan", new User("Susan", "susanPassword", Species.POULE));
    }

    @Override
    public Optional<User> get(final String id) {
        return Optional.ofNullable(this.users.get(id));
    }

    @Override
    public Set<String> getAll() {
        return this.users.keySet();
    }

    @Override
    public void save(final User user) throws NameAlreadyBoundException {
        final String key = user.getLogin();
        if (this.users.containsKey(key)) {
            throw new NameAlreadyBoundException(key);
        }
        this.users.put(key, user);
    }

    @Override
    public void update(final User user, final String[] params) {
        user.setPassword(Objects.requireNonNull(
                params[1], "Password cannot be null"));
        this.users.put(user.getLogin(), user);
    }

    @Override
    public void delete(final User user) {
        this.users.remove(user.getLogin());
    }
}
