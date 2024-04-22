package v1.amachon.member.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.member.entity.Profile;

import java.util.List;

@Data
@NoArgsConstructor
public class ProfileResponseDto {
    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;
    private String nickname;
    private List<String> techTags;
    private String regionTag;

    public ProfileResponseDto(Profile profile, String nickname, List<String> techTags, String regionTag) {
        this.introduction = profile.getIntroduction();
        this.profileImageUrl = profile.getProfileImageUrl();
        this.description = profile.getDescription();
        this.githubUrl = profile.getGithubUrl();
        this.blogUrl = profile.getBlogUrl();
        this.nickname = nickname;
        this.techTags = techTags;
        this.regionTag = regionTag;
    }
}
