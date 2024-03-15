package v1.amachon.project.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.member.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {
    private Long memberId;
    private Long teamMemberId;
    private String profileImageUrl;
    private String name;

    public TeamMemberResponse(Member member, Long teamMemberId) {
        this.memberId = member.getId();
        this.profileImageUrl = member.getProfile().getProfileImageUrl();
        this.name = member.getNickname();
        this.teamMemberId = teamMemberId;
    }
}
