package v1.amachon.project.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.member.entity.Member;
import v1.amachon.project.service.dto.project.ProjectModifyDto;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.ProjectTechTag;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<ProjectTechTag> techTags = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_tag_id")
    private RegionTag regionTag;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
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
    public void addImage(ProjectImage projectImage) {
        this.images.add(projectImage);
    }
    public void changeTechTag(List<ProjectTechTag> projectTechTags) {
        techTags.clear();
        techTags.addAll(projectTechTags);
    }

    public void changeImages(List<ProjectImage> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void modifyProject(ProjectModifyDto projectModifyDto, List<ProjectTechTag> techTags,
                              List<ProjectImage> images, RegionTag regionTag) {
        changeTechTag(techTags);
        changeImages(images);
        this.title = projectModifyDto.getTitle();
        this.description = projectModifyDto.getDescription();
        this.recruitDeadline = projectModifyDto.getRecruitDeadline();
        this.developPeriod = projectModifyDto.getDevelopPeriod();
        this.recruitNumber = projectModifyDto.getRecruitNumber();
        this.regionTag = regionTag;
    }
}
