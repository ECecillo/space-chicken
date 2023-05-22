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
        this.users.put("ECecillo", new User(
            "ECecillo",
            "root",
            Species.CHICKEN,
            "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));
        this.users.put("admin", new User(
            "admin",
            "root",
            Species.COWBOY,
            "https://cdn-icons-png.flaticon.com/512/3135/3135768.png"));
        this.users.put("Elfenwaar", new User(
            "Elfenwaar",
            "root",
            Species.COWBOY,
            "https://icons-for-free.com/download-icon-profile+profile+page+user+icon-1320186864367220794_512.png"));
        this.users.put("Melp", new User(
            "Melp",
            "root",
            Species.COWBOY,
            "https://cdn-icons-png.flaticon.com/512/3135/3135823.png"));
        this.users.put("John", new User(
            "John",
            "johnPassword",
            Species.CHICKEN)); // Personas
        this.users.put("system", new User("system", "system", Species.CHICKEN));
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

    public void updateInfos(final User user, final String[] params) {
        user.setPassword(Objects.requireNonNull(
                params[1], "Password cannot be null"));
        user.setImage(Objects.requireNonNull(
                params[2], "Image cannot be null"));
        this.users.put(user.getLogin(), user);
    }

    @Override
    public void delete(final User user) {
        this.users.remove(user.getLogin());
    }
}
