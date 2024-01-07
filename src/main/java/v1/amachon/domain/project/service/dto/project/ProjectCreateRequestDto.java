package v1.amachon.domain.project.service.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreateRequestDto {
    private String title;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitDeadline;
    private int recruitNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate developPeriod;
    private String regionTagName;
    private List<String> techTagNames;
}
