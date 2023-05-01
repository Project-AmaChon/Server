package v1.amachon.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.message.entity.MessageRoom;
import v1.amachon.domain.notification.entity.Notification;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public void addTechTag(MemberTechTag memberTechTag) {
        this.techTags.add(memberTechTag);
    }

    public void changeRegion(RegionTag regionTag) {
        this.regionTag = regionTag;
    }
}
