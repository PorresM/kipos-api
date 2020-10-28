package fr.mporres.kiposapi.controller.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private final String email;
    private final String role;
}
