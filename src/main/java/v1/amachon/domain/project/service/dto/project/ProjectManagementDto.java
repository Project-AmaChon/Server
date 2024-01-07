package v1.amachon.domain.project.service.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectManagementDto {
    List<ParticipatingProjectDto> participatingProjectDtoList;
    List<MyProjectDto> MyProjectDtoList;
}
