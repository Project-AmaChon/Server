package v1.amachon.domain.tags.dto.change;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChangeTagsDto {
    private ChangeTechTagDto changeTechTagDto;
    private ChangeRegionTagDto changeRegionTagDto;
}
