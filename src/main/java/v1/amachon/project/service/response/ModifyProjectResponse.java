package v1.amachon.project.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import v1.amachon.project.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyProjectResponse {
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitDeadline;
    private int recruitNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate developPeriod;
    private String regionTagName;
    private List<String> techTagNames;

    public ModifyProjectResponse(Project project) {
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.developPeriod = project.getDevelopPeriod();
        this.regionTagName = project.getRegionTag().getName();
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
    }
}
