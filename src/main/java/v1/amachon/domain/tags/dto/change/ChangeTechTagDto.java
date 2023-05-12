package v1.amachon.domain.tags.dto.change;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTechTagDto {
    private List<String> techTagName;
}
