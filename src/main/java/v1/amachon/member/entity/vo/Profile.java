package v1.amachon.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;

import javax.persistence.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;

    @Builder
    public Profile(String introduction, String profileImageUrl, String description, String githubUrl, String blogUrl) {
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.githubUrl = githubUrl;
        this.blogUrl = blogUrl;
    }

    public void changeProfile(UpdateProfileRequestDto profileDto) {
        this.introduction = profileDto.getIntroduction();
        this.description = profileDto.getDescription();
        this.githubUrl = profileDto.getGithubUrl();
        this.blogUrl = profileDto.getBlogUrl();
    }

    public void changeProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
