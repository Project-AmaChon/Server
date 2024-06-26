package v1.amachon.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.entity.vo.Authority;
import v1.amachon.member.entity.vo.Profile;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;
import v1.amachon.member.service.dto.join.JoinRequest;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.MemberTechTag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String nickname;
    private String password;

    @Embedded
    private Profile profile;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_tag_id")
    private RegionTag regionTag;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageRoom> messageRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTechTag> techTags = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String nickname, String password) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profile = Profile.builder().build();
        this.authorities.add(Authority.ofMember(this));
    }

    public static Member ofMember(JoinRequest joinRequest) {
        Member member = Member.builder()
                .nickname(joinRequest.getNickname())
                .email(joinRequest.getEmail())
                .password(joinRequest.getPassword())
                .build();
        return member;
    }

    public List<String> getRoles() {
        return authorities.stream()
                .map(Authority::getRole)
                .collect(Collectors.toList());
    }

    private void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public void changeTechTag(List<MemberTechTag> techTags) {
        this.techTags.clear();
        this.techTags.addAll(techTags);
    }

    public void changeRegionTag(RegionTag regionTag) {
        this.regionTag = regionTag;
    }

    public void changeProfile(UpdateProfileRequestDto updateProfileRequest) {
        this.profile.changeProfile(updateProfileRequest);
    }

    public void changeProfileImage(String imageUrl) {
        this.profile.changeProfileImage(imageUrl);
    }
}
