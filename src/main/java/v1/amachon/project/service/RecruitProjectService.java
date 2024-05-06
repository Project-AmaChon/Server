package v1.amachon.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.config.security.util.SecurityUtils;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.entity.Member;
import v1.amachon.project.repository.MemberRecommendRepo;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.member.service.dto.RecommendCond;
import v1.amachon.project.entity.Project;
import v1.amachon.project.entity.RecruitManagement;
import v1.amachon.project.entity.TeamMember;
import v1.amachon.project.repository.ProjectRepository;
import v1.amachon.project.repository.RecruitManagementRepository;
import v1.amachon.project.repository.TeamMemberRepository;
import v1.amachon.project.service.response.RecommendMemberResponse;
import v1.amachon.project.service.response.RecruitManagementResponse;
import v1.amachon.project.service.exception.FailureProjectModifyException;
import v1.amachon.project.service.exception.NotFoundProjectException;
import v1.amachon.project.service.exception.NotFoundRecruitManagementException;
import v1.amachon.project.service.exception.NotFoundTeamMemberException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RecruitProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final RecruitManagementRepository recruitManagementRepository;
    private final MemberRecommendRepo memberRecommendRepo;
    private final TeamMemberRepository teamMemberRepository;

    public void applyForProject(Long projectId) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        Project project = projectRepository.findByIdFetchRecruitments(projectId)
                .orElseThrow(NotFoundProjectException::new);

        project.apply(currentMember);
    }

    public void recruitAccept(Long recruitManagementId) {
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId)
                .orElseThrow(NotFoundRecruitManagementException::new);

        verifyProjectOwner(recruitManagement.getProject());

        teamMemberRepository.save(new TeamMember(recruitManagement.getProject(), recruitManagement.getMember()));
        recruitManagement.expired();
    }

    public void recruitReject(Long recruitManagementId) {
        RecruitManagement recruitManagement = recruitManagementRepository.findByIdFetch(recruitManagementId)
                .orElseThrow(NotFoundRecruitManagementException::new);

        verifyProjectOwner(recruitManagement.getProject());

        recruitManagement.expired();
    }

    public void kickTeamMember(Long teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(NotFoundTeamMemberException::new);

        verifyProjectOwner(teamMember.getProject());

        teamMember.expired();
    }

    @Transactional(readOnly = true)
    public List<RecruitManagementResponse> getRecruitList(Long projectId) {
        Project project = projectRepository.getRecruitListFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);

        verifyProjectOwner(project);

        return project.getRecruitManagements().stream()
                .map(RecruitManagementResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecommendMemberResponse> getRecommendMember(Long projectId) {
        Project project = projectRepository.findByIdFetch(projectId)
                .orElseThrow(NotFoundProjectException::new);

        verifyProjectOwner(project);

        RecommendCond cond = new RecommendCond(project);

        return memberRecommendRepo.getRecommendMemberByCond(cond);
    }

    private void verifyProjectOwner(Project project) {
        Member currentMember = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);
        if (currentMember.getId().equals(project.getLeader().getId())) {
            throw new FailureProjectModifyException();
        }
    }
}
