package v1.amachon.domain.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "team_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public TeamMember(Project project, Member member) {
        this.project = project;
        this.member = member;
    }
}
