package v1.amachon.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Profile;

import java.util.List;

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
    private String nickname;
    private List<String> techTags;
    private String regionTag;

    public ProfileDto(Profile profile, List<String> techTags, String regionTag) {
        this.introduction = profile.getIntroduction();
        this.profileImageUrl = profile.getProfileImageUrl();
        this.description = profile.getDescription();
        this.githubUrl = profile.getGithubUrl();
        this.blogUrl = profile.getBlogUrl();
        this.techTags = techTags;
        this.regionTag = regionTag;
    }
}
