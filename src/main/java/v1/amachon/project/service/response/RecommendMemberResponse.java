package v1.amachon.project.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendMemberResponse {
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private List<String> techTagNames;

    public RecommendMemberResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getNickname();
        this.profileImageUrl = member.getProfile().getProfileImageUrl();
        this.techTagNames = member.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());

    }
}
