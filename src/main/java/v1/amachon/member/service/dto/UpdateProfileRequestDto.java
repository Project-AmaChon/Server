package v1.amachon.member.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {
    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;
    private String nickname;
}
