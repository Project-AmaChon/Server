package v1.amachon.domain.project.service.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.ProjectImage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectModifyDto {
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitDeadline;
    private int recruitNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate developPeriod;
    private String regionTagName;
    private List<String> techTagNames;
    private List<String> imageUrls;

    public ProjectModifyDto(Project project) {
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.recruitDeadline = project.getRecruitDeadline();
        this.recruitNumber = project.getRecruitNumber();
        this.developPeriod = project.getDevelopPeriod();
        this.regionTagName = project.getRegionTag().getName();
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
        this.imageUrls = project.getImages().stream().map(ProjectImage::getImageUrl).collect(Collectors.toList());
    }
}
