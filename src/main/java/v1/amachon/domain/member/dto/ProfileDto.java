package v1.amachon.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;
}
