package v1.amachon.member.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.project.entity.Project;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendCond {
    public List<String> regionTagNames;
    public List<String> techTagNames;

    public RecommendCond(Project project) {
        this.regionTagNames = Arrays.asList(project.getRegionTag().getName());
        this.techTagNames = project.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
    }
}
