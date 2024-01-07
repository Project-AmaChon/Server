package v1.amachon.tags.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.tags.entity.regiontag.RegionTag;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionTagDto {
    private String name;
    private List<String> children;

    public RegionTagDto(RegionTag regionTag) {
        this.name = regionTag.getName();
        this.children = regionTag.getChildren().stream().map(RegionTag::getName).collect(Collectors.toList());
    }
}
