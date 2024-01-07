package v1.amachon.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitManagement extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "recruit_management_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public RecruitManagement(Member member, Project project) {
        this.member = member;
        this.project = project;
        project.addRecruitManagement(this);
    }
}
