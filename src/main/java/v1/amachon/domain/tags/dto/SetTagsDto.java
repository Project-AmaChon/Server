package v1.amachon.domain.tags.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetTagsDto {
    private String regionTagName;
    private List<String> techTagName;
}
