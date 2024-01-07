package v1.amachon.project.service.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.project.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private Long projectId;
    private String title;
    private LocalDate recruitDeadline;
    private int recruitNumber;
    private int currentNumber;
    private List<String> tagNames;

    public ProjectDto(Project project) {
        this.projectId = project.getId();
        this.title = project.getTitle();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.currentNumber = project.getTeamMembers().size();
        this.tagNames = project.getTechTags().stream().map(p -> p.getTechTag().getName()).collect(Collectors.toList());
    }
}
