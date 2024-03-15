package v1.amachon.project.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import v1.amachon.member.entity.Member;
import v1.amachon.project.entity.RecruitManagement;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitManagementResponse {
    private Long memberId;
    private Long recruitManagementId;
    private String name;
    private String profileImageUrl;
    private List<String> techTagNames;

    public RecruitManagementResponse(RecruitManagement recruitManagement) {
        Member member = recruitManagement.getMember();
        this.memberId = member.getId();
        this.name = member.getNickname();
        this.recruitManagementId = recruitManagement.getId();
        this.profileImageUrl = member.getProfile().getProfileImageUrl();
        this.techTagNames = member.getTechTags().stream().map(t -> t.getTechTag().getName()).collect(Collectors.toList());
    }
}
