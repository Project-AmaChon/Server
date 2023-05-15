package v1.amachon.domain.project.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;

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
    private Long regionTagId;
    private List<Long> techTagIds;
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
        this.regionTagId = project.getRegionTag().getId();
        this.techTagIds = project.getTechTags().stream().map(ProjectTechTag::getId).collect(Collectors.toList());
        this.imageUrls = project.getImages().stream().map(ProjectImage::getImageUrl).collect(Collectors.toList());
        this.teamMembers = project.getTeamMembers().stream().map(t -> new TeamMemberDto(t.getMember())).collect(Collectors.toList());
    }
}
