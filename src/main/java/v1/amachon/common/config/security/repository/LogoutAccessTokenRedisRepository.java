package v1.amachon.common.config.security.repository;

import org.springframework.data.repository.CrudRepository;
import v1.amachon.common.config.security.token.LogoutAccessToken;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
