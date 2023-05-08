package v1.amachon.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ProjectCreateResponseDto {
  private Long id;
  private String title;
  private String description;
  private LocalDate recruitDeadline;
  private Integer recruitNumber;
  private LocalDate developPeriod;
  private Long leaderId;
  private Long regionTagId;
  private List<TechTag> techTags;
  private List<String> imageUrls;
}
