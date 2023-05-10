package v1.amachon.domain.tags.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.tags.entity.techtag.TechTag;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechTagDto {
    private String name;
    private List<String> children;

    public TechTagDto(TechTag techTag) {
        this.name = techTag.getName();
        this.children = techTag.getChildren().stream().map(TechTag::getName).collect(Collectors.toList());
    }
}
