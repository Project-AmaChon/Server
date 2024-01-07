package v1.amachon.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.tags.service.dto.change.ChangeTechTagDto;
import v1.amachon.tags.service.dto.TechTagDto;
import v1.amachon.tags.entity.techtag.MemberTechTag;
import v1.amachon.tags.entity.techtag.TechTag;
import v1.amachon.tags.repository.MemberTechTagRepository;
import v1.amachon.tags.repository.TechTagRepository;
import v1.amachon.tags.service.exception.NotFoundTechTagException;
import v1.amachon.common.config.security.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechTagService {

    private final TechTagRepository techTagRepository;
    private final MemberRepository memberRepository;
    private final MemberTechTagRepository memberTechTagRepository;

    @Cacheable(value = "techTags")
    public List<TechTagDto> getAllTechTags() {
        List<TechTag> tags = techTagRepository.findByDepth(0);
        List<TechTagDto> techTagListDto = tags.stream().map(TechTagDto::new).collect(Collectors.toList());
        return techTagListDto;
    }

    @Cacheable(value = "techTag", key = "#tagName")
    public TechTagDto getTechTag(String tagName)  {
        TechTag tag = techTagRepository.findByName(tagName)
                .orElseThrow(NotFoundTechTagException::new);
        return new TechTagDto(tag);
    }

    public void changeTechTags(ChangeTechTagDto changeTechTagDto)  {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(UnauthorizedException::new);

        for (String tagName : changeTechTagDto.getTechTagName()) {
            TechTag tag = techTagRepository.findByName(tagName)
                    .orElseThrow(NotFoundTechTagException::new);
            memberTechTagRepository.save(new MemberTechTag(member, tag));
        }
    }
}
