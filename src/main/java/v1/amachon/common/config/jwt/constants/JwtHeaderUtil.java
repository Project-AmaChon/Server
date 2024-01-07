package v1.amachon.common.config.jwt.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtHeaderUtil {

    GRANT_TYPE("JWT 타입 / Bearer ", "Bearer ");

    private String description;
    private String value;
}