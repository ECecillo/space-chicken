package fr.univlyon1.m1if.m1if13.repository.dao;

import fr.univlyon1.m1if.m1if13.model.Species;
import fr.univlyon1.m1if.m1if13.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

import javax.naming.NameAlreadyBoundException;

@Component
public class UserDao implements Dao<User> {

    protected Map<String, User> users = new HashMap<>();

    public UserDao() {
        this.users.put("ECecillo", new User("Enzo", Species.POULE, "root"));
        this.users.put("John", new User("John", Species.COWBOY, "john@domain.com"));
        this.users.put("Susan", new User("Susan", Species.POULE, "susan@domain.com"));
    }

    @Override
    public Optional<User> get(String id) {
        return Optional.ofNullable(this.users.get(id));
    }

    @Override
    public Set<String> getAll() {
        return this.users.keySet();
    }

    @Override
    public void save(User user) throws NameAlreadyBoundException {
        final String key = user.getLogin();
        if (this.users.containsKey(key)) {
            throw new NameAlreadyBoundException(key);
        }
        this.users.put(key, user);
    }

    @Override
    public void update(User user, String[] params) {
        user.setPassword(Objects.requireNonNull(
                params[1], "Password cannot be null"));
        this.users.put(user.getLogin(), user);
    }

    @Override
    public void delete(User user) {
        this.users.remove(user.getLogin());
    }
}
