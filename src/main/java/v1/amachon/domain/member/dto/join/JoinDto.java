package v1.amachon.domain.member.dto.join;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinDto {
    private String email;
    private String nickname;
    private String password;
}