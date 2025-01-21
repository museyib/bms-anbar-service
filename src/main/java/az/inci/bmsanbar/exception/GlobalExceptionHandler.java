package az.inci.bmsanbar.exception;

import az.inci.bmsanbar.model.v4.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static az.inci.bmsanbar.Utilities.getMessage;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OperationNotCompletedException.class)
    public ResponseEntity<Response> handleOperationNotCompletedException(OperationNotCompletedException e) {
        String message = getMessage(e);
        log.error(message);
        return ResponseEntity.ok(Response.getUserErrorResponse(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGeneralException(Exception e) {
        String message = getMessage(e);
        log.error(message);
        return ResponseEntity.ok(Response.getServerErrorResponse(message));
    }
}
