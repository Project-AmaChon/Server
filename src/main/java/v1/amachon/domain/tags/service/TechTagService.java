package v1.amachon.domain.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.tags.dto.change.ChangeTechTagDto;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.MemberTechTagRepository;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;
import v1.amachon.global.config.security.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechTagService {

    private final TechTagRepository techTagRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechTagRepository projectTechTagRepository;
    private final MemberRepository memberRepository;
    private final MemberTechTagRepository memberTechTagRepository;

    @Cacheable(value = "techTags")
    public List<TechTagDto> getAllTechTags() {
        List<TechTag> tags = techTagRepository.findByDepth(0);
        List<TechTagDto> techTagListDto = tags.stream().map(TechTagDto::new).collect(Collectors.toList());
        return techTagListDto;
    }

    @Cacheable(value = "techTag", key = "#tagName")
    public TechTagDto getTechTag(String tagName) throws BaseException {
        TechTag tag = techTagRepository.findByName(tagName).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_TAG)
        );
        return new TechTagDto(tag);
    }

    public void changeTechTags(ChangeTechTagDto changeTechTagDto) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(BaseResponseStatus.UNAUTHORIZED)
        );

        for (String tagName : changeTechTagDto.getTechTagName()) {
            TechTag tag = techTagRepository.findByName(tagName).orElseThrow(
                    () -> new BaseException(BaseResponseStatus.INVALID_TAG)
            );
            memberTechTagRepository.save(new MemberTechTag(member, tag));
        }
    }
}
