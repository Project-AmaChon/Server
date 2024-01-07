package v1.amachon.member.repository.auth;

import org.springframework.data.repository.CrudRepository;
import v1.amachon.member.entity.auth.LogoutAccessToken;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
