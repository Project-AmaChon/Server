package v1.amachon.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Profile;

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

    public ProfileDto(Profile profile) {
        this.introduction = profile.getIntroduction();
        this.profileImageUrl = profile.getProfileImageUrl();
        this.description = profile.getDescription();
        this.githubUrl = profile.getGithubUrl();
        this.blogUrl = profile.getBlogUrl();
    }
}
