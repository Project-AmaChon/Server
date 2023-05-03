package v1.amachon.global.config.jwt.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum JwtExpiration {

    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 3개월", 1000L * 60 * 60 * 24 * 30 * 3),
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 6개월", 1000L * 60 * 60 * 24 * 30 * 6);

    private String description;
    private Long value;
}