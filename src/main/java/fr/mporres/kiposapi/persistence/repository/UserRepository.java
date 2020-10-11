package fr.mporres.kiposapi.persistence.repository;

import fr.mporres.kiposapi.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
