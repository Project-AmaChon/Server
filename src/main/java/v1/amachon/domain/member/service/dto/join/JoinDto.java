package v1.amachon.domain.member.service.dto.join;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {
    @Email
    private String email;
    private String nickname;
    private String password;
}
