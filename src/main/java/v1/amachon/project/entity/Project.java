package v1.amachon.project.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.member.entity.Member;
import v1.amachon.project.service.request.ModifyProjectRequest;
import v1.amachon.project.service.exception.ProjectApplyDeniedException;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitDeadline;
    private int recruitNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate developPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProjectTechTag> techTags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_tag_id")
    private RegionTag regionTag;

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RecruitManagement> recruitManagements = new ArrayList<>();

    @Builder
    public Project(String title, String description, LocalDate recruitDeadline, int recruitNumber,
        LocalDate developPeriod, Member leader, RegionTag regionTag) {
        this.title = title;
        this.description = description;
        this.recruitDeadline = recruitDeadline;
        this.recruitNumber = recruitNumber;
        this.developPeriod = developPeriod;
        this.leader = leader;
        this.regionTag = regionTag;
    }

    public void addTeamMember(TeamMember teamMember) {
        this.teamMembers.add(teamMember);
    }
    public void addRecruitManagement(RecruitManagement recruitManagement) {
        recruitManagements.add(recruitManagement);
    }
    public void addTechTag(ProjectTechTag projectTechTag) {
        techTags.add(projectTechTag);
    }
    public void modifyProject(ModifyProjectRequest modifyProjectRequest, RegionTag regionTag) {
        techTags.clear();
        this.title = modifyProjectRequest.getTitle();
        this.description = modifyProjectRequest.getDescription();
        this.recruitDeadline = modifyProjectRequest.getRecruitDeadline();
        this.developPeriod = modifyProjectRequest.getDevelopPeriod();
        this.recruitNumber = modifyProjectRequest.getRecruitNumber();
        this.regionTag = regionTag;
    }

    public void delete() {
        deleteRecruitManagement();
        deleteTeamMember();
        deleteProjectTechTag();
        this.expired();
    }

    public void deleteRecruitManagement() {
        for (RecruitManagement recruitManagement : recruitManagements) recruitManagement.expired();
    }

    public void deleteTeamMember() {
        for (TeamMember teamMember : teamMembers) teamMember.expired();
    }

    public void deleteProjectTechTag() {
        for (ProjectTechTag techTag : techTags) techTag.expired();
    }

    public void apply(Member member) {
        List<Long> teamMembersId = teamMembers.stream().map(TeamMember::getId).collect(Collectors.toList());
        List<Long> applicantsId = recruitManagements.stream().map(r -> r.getMember().getId()).collect(Collectors.toList());

        if (leader.getId().equals(member.getId())) {
            throw new BadRequestException();
        }

        if (teamMembersId.contains(member.getId()) || applicantsId.contains(member.getId())) {
            throw new ProjectApplyDeniedException();
        }

        recruitManagements.add(new RecruitManagement(member, this));
    }
}
