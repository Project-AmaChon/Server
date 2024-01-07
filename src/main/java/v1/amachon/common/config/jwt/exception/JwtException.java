package v1.amachon.common.config.jwt.exception;

import v1.amachon.common.exception.UnauthorizedException;

public class JwtException extends UnauthorizedException {

    public JwtException(String message) {
        super(message);
    }
}
