package v1.amachon.project.service.dto.project;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.project.service.dto.TeamMemberDto;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.ProjectImage;
import v1.amachon.project.entity.TeamMember;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
    private Long projectId;
    private String title;
    private String description;
    private LocalDate recruitDeadline;
    private int recruitNumber;
    private LocalDate developPeriod;
    private Long leaderId;
    private String regionTagName;
    private List<String> techTagNames;
    private List<String> imageUrls;
    // 추가: 프로젝트 팀에 참가 중인 인원들의 목록
    private List<TeamMemberDto> teamMembers;

    public ProjectDetailDto(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.developPeriod = project.getDevelopPeriod();
        this.leaderId = project.getLeader().getId();
        this.regionTagName = project.getRegionTag().getName();
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
        this.imageUrls = project.getImages().stream().map(ProjectImage::getImageUrl).collect(Collectors.toList());
        this.teamMembers = project.getTeamMembers().stream().map(t -> new TeamMemberDto(t.getMember(), t.getId())).collect(Collectors.toList());
    }

    public ProjectDetailDto(Project project, List<TeamMember> teamMember) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.developPeriod = project.getDevelopPeriod();
        this.leaderId = project.getLeader().getId();
        this.regionTagName = project.getRegionTag().getName();
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
        this.imageUrls = project.getImages().stream().map(ProjectImage::getImageUrl).collect(Collectors.toList());
        this.teamMembers = teamMember.stream().map(t -> new TeamMemberDto(t.getMember(), t.getId())).collect(Collectors.toList());
    }
}
