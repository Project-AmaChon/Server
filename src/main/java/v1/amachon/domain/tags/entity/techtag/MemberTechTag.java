package v1.amachon.domain.tags.entity.techtag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTechTag {

    @Id @GeneratedValue
    @Column(name = "member_tech_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_tag")
    private TechTag techTag;

    public MemberTechTag(Member member, TechTag techTag) {
        this.member = member;
        this.techTag = techTag;
        member.addTechTag(this);
    }
}
