package v1.amachon.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.member.service.dto.ProfileResponseDto;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;
    private String introduction;
    private String profileImageUrl;
    private String description;
    private String githubUrl;
    private String blogUrl;

    @Builder
    public Profile(ProfileResponseDto profileDto) {
        this.introduction = profileDto.getIntroduction();
        this.profileImageUrl = profileDto.getProfileImageUrl();
        this.description = profileDto.getDescription();
        this.githubUrl = profileDto.getGithubUrl();
        this.blogUrl = profileDto.getBlogUrl();
    }

    public void init() {
        this.blogUrl = "";
        this.description = "";
        this.githubUrl = "";
        this.profileImageUrl = "";
        this.introduction = "";
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
