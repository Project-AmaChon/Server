package v1.amachon.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.common.exception.InvalidFormatException;
import v1.amachon.common.exception.NotFoundException;
import v1.amachon.common.exception.UnauthorizedException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class CommonControllerAdvice {

    @ExceptionHandler(value = {BadRequestException.class, InvalidFormatException.class,
            MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        log.debug("HandleBadRequest : {}", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(e.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(Exception e) {
        log.debug("UnauthorizedException : {}", e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(e.getMessage(), UNAUTHORIZED.value()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final Exception e) {
        log.debug("NotFoundException : {}", e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(e.getMessage(), NOT_FOUND.value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(RuntimeException e) {
        log.debug("RuntimeException : {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR.value()));
    }
}
