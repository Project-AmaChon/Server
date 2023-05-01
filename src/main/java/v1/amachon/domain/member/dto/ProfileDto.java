package v1.amachon.domain.member.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;
}
