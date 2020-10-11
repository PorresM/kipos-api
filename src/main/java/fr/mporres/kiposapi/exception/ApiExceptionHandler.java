package fr.mporres.kiposapi.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ErrorResponse handleException(HttpServletRequest request, HttpServletResponse response, Throwable t) throws Throwable {
        if (!(t instanceof ApiException)) {
            throw t;
        }

        ApiException apiException = (ApiException) t;
        response.setStatus(apiException.getHttpStatus().value());
        response.setContentType("application/json");

        return ErrorResponse.builder()
            .error(apiException.getHttpStatus().getReasonPhrase())
            .status(apiException.getHttpStatus().value())
            .message(apiException.getMessage())
            .path(request.getRequestURI())
            .timestamp(new Date()).build();
    }
}
