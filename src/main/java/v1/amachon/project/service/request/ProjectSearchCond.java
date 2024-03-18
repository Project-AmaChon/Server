package v1.amachon.project.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSearchCond {

    public String keyword;
    public Set<String> regionTagNames;
    public Set<String> techTagNames;
}
