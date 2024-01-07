package v1.amachon.domain.project.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.domain.member.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberDto {
    private Long memberId;
    private Long teamMemberId;
    private String profileImageUrl;
    private String name;

    public TeamMemberDto(Member member, Long teamMemberId) {
        this.memberId = member.getId();
        this.profileImageUrl = member.getProfile().getProfileImageUrl();
        this.name = member.getNickname();
        this.teamMemberId = teamMemberId;
    }
}
