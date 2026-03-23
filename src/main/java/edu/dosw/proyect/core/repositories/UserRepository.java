package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<Long, User> dataStore = new HashMap<>();

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId((long) (dataStore.size() + 1));
        }
        dataStore.put(user.getId(), user);
        return user;
    }
}
