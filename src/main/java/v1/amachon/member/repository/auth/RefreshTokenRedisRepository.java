package v1.amachon.member.repository.auth;

import org.springframework.data.repository.CrudRepository;
import v1.amachon.member.entity.auth.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
