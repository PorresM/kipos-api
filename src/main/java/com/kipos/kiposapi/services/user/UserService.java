package com.kipos.kiposapi.services.user;

import com.kipos.kiposapi.enums.UserRole;
import com.kipos.kiposapi.persistence.entity.User;
import com.kipos.kiposapi.persistence.repository.UserRepository;
import com.kipos.kiposapi.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service that manages /users calls
 */
@Component
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * User creation
     *
     * @param email     user's email
     * @param password  user's password
     * @param userRole  user's role
     * @return the corresponding user if it exists, empty otherwise
     */
    public Optional<User> createUser(String email, String password, UserRole userRole) {
        Optional<User> uEmail = userRepository.findByEmail(email);
        if (uEmail.isPresent()) {
            return Optional.empty();
        }

        User user = User.builder()
            .email(email)
            .password(PasswordUtils.PASSWORD_ENCODER.encode(password))
            .role(userRole)
            .build();

        return Optional.of(userRepository.save(user));
    }

    /**
     * User fetching by id
     *
     * @param idUser    user's id
     * @return the corresponding user if it exists, empty otherwise
     */
    public Optional<User> getUserById(long idUser) {
        return userRepository.findById(idUser);
    }

    /**
     * User fetching by id email
     *
     * @param email     user's email
     * @return the corresponding user if it exists, empty otherwise
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * User deletion
     *
     * @param idUser    user's id
     */
    public void deleteUser(long idUser) {
        getUserById(idUser).ifPresent(userRepository::delete);
    }

    /**
     * Determines whether the user passed as a parameter has the right to delete the user associated with the identifier
     *
     * @param user              user requesting deletion
     * @param idUserToDelete    the id of the user to be deleted
     * @return true if so, false otherwise
     */
    public boolean hasRightToDeleteUser(User user, long idUserToDelete) {
        boolean isSameUser = !user.getId().equals(idUserToDelete);
        if (isSameUser) {
            LOGGER.error("The {} user has requested the deletion of his user: operation not permitted.", user.getId());
        }
        return !isSameUser;
    }
}
