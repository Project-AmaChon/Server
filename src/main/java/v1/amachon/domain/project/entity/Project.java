package v1.amachon.domain.project.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate recruitDeadline;
    private int recruitNumber;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "project_image_id")
    private List<ProjectImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, orphanRemoval = true)
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
}
