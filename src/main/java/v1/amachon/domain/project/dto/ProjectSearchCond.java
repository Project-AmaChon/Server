package v1.amachon.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSearchCond {

    public String keyword;
    public List<String> regionTagNames;
    public List<String> techTagNames;
}
