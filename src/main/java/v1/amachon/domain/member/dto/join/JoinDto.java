package v1.amachon.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinDto {
    private String email;
    private String nickname;
    private String password;
}
