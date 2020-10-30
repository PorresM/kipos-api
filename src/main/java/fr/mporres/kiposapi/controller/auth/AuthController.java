package fr.mporres.kiposapi.controller.auth;

import fr.mporres.kiposapi.controller.auth.request.LoginRequest;
import fr.mporres.kiposapi.controller.auth.request.SignupRequest;
import fr.mporres.kiposapi.controller.auth.response.LoginResponse;
import fr.mporres.kiposapi.controller.user.response.UserResponse;
import fr.mporres.kiposapi.enums.UserRole;
import fr.mporres.kiposapi.exception.ApiException;
import fr.mporres.kiposapi.persistence.entity.User;
import fr.mporres.kiposapi.services.auth.AuthService;
import fr.mporres.kiposapi.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * User authentication WS
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * login WS
     */
    @PostMapping(value = "/login")
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest requestUser) {
        User dbUser;
        if (requestUser.getEmail() == null || requestUser.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials");
        }

        dbUser = userService.getUserByEmail(requestUser.getEmail()).orElseThrow(
            () -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials")
        );

        return authService.getToken(dbUser, requestUser.getPassword())
            .map(
                token -> LoginResponse.builder()
                .token(token)
                .user(
                    UserResponse.builder()
                        .email(dbUser.getEmail())
                        .role(dbUser.getRole().name())
                        .build()
                ).build()
            ).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Authentication Failed: Bad credentials"));
    }

    /**
     * User register
     *
     * @param request the user creation request
     */
    @PostMapping(value = "/register") // TODO: valid is not working
    public void createUser(@RequestBody @Valid SignupRequest request) {
        userService.createUser(request.getEmail(), request.getPassword(), UserRole.USER)
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Email already used"));
        LOGGER.info("User {} created", request.getEmail());
    }
}
