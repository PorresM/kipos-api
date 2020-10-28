package fr.mporres.kiposapi.controller.auth.response;

import fr.mporres.kiposapi.controller.user.response.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private final String token;
    private final UserResponse user;
}
