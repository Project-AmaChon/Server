package v1.amachon.domain.member.repository.auth;

import org.springframework.data.repository.CrudRepository;
import v1.amachon.domain.member.entity.auth.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
