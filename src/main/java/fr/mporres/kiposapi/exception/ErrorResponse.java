package fr.mporres.kiposapi.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorResponse {
    private final Date timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
