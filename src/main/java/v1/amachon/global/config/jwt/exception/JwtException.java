package v1.amachon.global.config.jwt.exception;

import v1.amachon.domain.common.exception.UnauthorizedException;

public class JwtException extends UnauthorizedException {

    public JwtException(String message) {
        super(message);
    }
}
