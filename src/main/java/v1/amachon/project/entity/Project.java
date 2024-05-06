package v1.amachon.project.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.common.exception.BadRequestException;
import v1.amachon.member.entity.Member;
import v1.amachon.project.service.request.UpdateProjectRequest;
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
@Table(name = "project", indexes = {@Index(name = "idx_project_id", columnList = "project_id")})
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

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProjectTechTag> techTags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_tag_id")
    private RegionTag regionTag;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
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
    public void updateTechTags(List<ProjectTechTag> projectTechTags) {
        techTags.clear();
        techTags.addAll(projectTechTags);
    }

    public void updateRegionTag(RegionTag regionTag) {
        this.regionTag = regionTag;
    }
    public void updateProject(UpdateProjectRequest updateProjectRequest) {
        this.title = updateProjectRequest.getTitle();
        this.description = updateProjectRequest.getDescription();
        this.recruitDeadline = updateProjectRequest.getRecruitDeadline();
        this.developPeriod = updateProjectRequest.getDevelopPeriod();
        this.recruitNumber = updateProjectRequest.getRecruitNumber();
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
        List<String> teamMembersEmail = teamMembers.stream().map(t -> t.getMember().getEmail()).collect(Collectors.toList());
        List<String> applicantsEmail = recruitManagements.stream().map(r -> r.getMember().getEmail()).collect(Collectors.toList());

        if (leader.getEmail().equals(member.getEmail())) {
            throw new BadRequestException();
        }

        if (teamMembersEmail.contains(member.getEmail()) || applicantsEmail.contains(member.getEmail())) {
            throw new ProjectApplyDeniedException();
        }

        recruitManagements.add(new RecruitManagement(member, this));
    }
}
