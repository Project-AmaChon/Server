package v1.amachon.project.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectManagementResponse {
    List<ParticipatingProjectResponse> participatingProjectDtoList;
    List<MyProjectResponse> MyProjectDtoList;
}
