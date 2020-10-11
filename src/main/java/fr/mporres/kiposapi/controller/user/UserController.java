package fr.mporres.kiposapi.controller.user;

import fr.mporres.kiposapi.controller.user.request.CreateUserRequest;
import fr.mporres.kiposapi.controller.user.response.UserResponse;
import fr.mporres.kiposapi.enums.UserRole;
import fr.mporres.kiposapi.exception.ApiException;
import fr.mporres.kiposapi.persistence.entity.User;
import fr.mporres.kiposapi.services.auth.AuthService;
import fr.mporres.kiposapi.services.user.UserService;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * User WS
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * User fetching
     *
     * @param idUser user's id
     * @return the corresponding user if it exists, 400 error otherwise
     */
    @PreAuthorize("hasAuthority('USER_GET')")
    @GetMapping(value = "/{idUser}")
    public @ResponseBody
    UserResponse getUser(@PathVariable(value = "idUser") long idUser) {
        LOGGER.info("Fetching user {}", idUser);
        return userService.getUserById(idUser)
            .map(user -> UserResponse.builder().login(user.getEmail()).role(user.getRole().name()).build())
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, String.format("Unable to retrieve user %d. Unknown user.", idUser)));
    }

    /**
     * User creation
     *
     * @param request the user creation request
     */
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping(value = "")
    public void createUser(@RequestBody @Valid CreateUserRequest request) {
        String role = request.getRole().toUpperCase();
        if (!EnumUtils.isValidEnum(UserRole.class, role)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Non-existent role");
        }
        userService.createUser(request.getEmail(), request.getPassword(), UserRole.valueOf(role))
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Email already used"));
        LOGGER.info("User {} created", request.getEmail());
    }

    /**
     * User deletion
     *
     * @param idUser user's id
     */
    @PreAuthorize("hasAuthority('USER_DELETE') && @userService.hasRightToDeleteUser(principal, #idUser)")
    @DeleteMapping(value = "/{idUser}")
    public void deleteUser(@PathVariable(value = "idUser") long idUser) {
        LOGGER.info("user {} deleted", idUser);
        userService.deleteUser(idUser);
    }

    /**
     * Logged user fetching
     *
     * @return the logged user
     */
    @PreAuthorize("hasAuthority('USER_GET_SELF')")
    @GetMapping(value = "/self")
    public @ResponseBody
    UserResponse getCurentUser() {
        User loggedInUser = authService.getAuthUser();
        LOGGER.info("Logged user {} fetched", loggedInUser.getId());
        return UserResponse.builder().login(loggedInUser.getEmail()).role(loggedInUser.getRole().name()).build();
    }

}
