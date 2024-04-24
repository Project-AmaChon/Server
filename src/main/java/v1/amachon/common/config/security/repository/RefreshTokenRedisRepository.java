package v1.amachon.common.config.security.repository;

import org.springframework.data.repository.CrudRepository;
import v1.amachon.common.config.security.token.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
