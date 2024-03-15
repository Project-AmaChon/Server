package v1.amachon.project.service.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.project.service.request.TeamMemberDto;
import v1.amachon.project.entity.Project;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
    private Long projectId;
    private String title;
    private String description;
    private LocalDate recruitDeadline;
    private int recruitNumber;
    private LocalDate developPeriod;
    private Long leaderId;
    private String regionTagName;
    private List<String> techTagNames;
    private List<TeamMemberDto> teamMembers;

    public ProjectDetailResponse(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.developPeriod = project.getDevelopPeriod();
        this.leaderId = project.getLeader().getId();
        this.regionTagName = project.getRegionTag().getName();
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
        this.teamMembers = project.getTeamMembers().stream().map(t -> new TeamMemberDto(t.getMember(), t.getId())).collect(Collectors.toList());
    }
}
