package v1.amachon.domain.project.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate recruitDeadline;
    private int recruitNumber;
    private LocalDate developPeriod;
    private Long leaderId;
    private Long regionTagId;
    private List<Long> techTagIds;
    private List<String> imageUrls;
    // 추가: 프로젝트 팀에 참가 중인 인원들의 목록
    private List<Member> teamMembers;
}
