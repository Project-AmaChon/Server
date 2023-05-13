package v1.amachon.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCreateRequestDto {
  private String title;
  private String description;
  private LocalDate recruitDeadline;
  private int recruitNumber;
  private LocalDate developPeriod;
  private Long leaderId;
  private Long regionTagId;
  private List<Long> techTagIds;
  private List<String> imageUrls;
}
