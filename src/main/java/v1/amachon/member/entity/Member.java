package v1.amachon.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;
import v1.amachon.member.service.dto.join.JoinDto;
import v1.amachon.message.entity.MessageRoom;
import v1.amachon.notification.entity.Notification;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.MemberTechTag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    public enum NotificationOption {
        ON, OFF;
    }

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String nickname;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private NotificationOption notificationOption;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_tag_id")
    private RegionTag regionTag;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "notification_id")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MessageRoom> messageRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemberTechTag> techTags = new ArrayList<>();

    @Builder
    public Member(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.notificationOption = NotificationOption.ON;
        this.profile = new Profile();
        this.profile.init();
    }

    public static Member ofMember(JoinDto joinDto) {
        Member member = Member.builder()
                .nickname(joinDto.getNickname())
                .email(joinDto.getEmail())
                .password(joinDto.getPassword())
                .build();
        member.addAuthority(Authority.ofMember(member));
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

    public void changeRegion(RegionTag regionTag) {
        this.regionTag = regionTag;
    }

    public void changeProfile(UpdateProfileRequestDto updateProfileRequest) {
        this.profile.changeProfile(updateProfileRequest);
    }
}
