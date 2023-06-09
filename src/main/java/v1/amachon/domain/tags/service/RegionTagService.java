package v1.amachon.domain.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.tags.dto.RegionTagDto;
import v1.amachon.domain.tags.dto.change.ChangeRegionTagDto;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.global.config.security.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RegionTagService {

    private final RegionTagRepository regionTagRepository;
    private final MemberRepository memberRepository;

    @Cacheable(value = "regionTags")
    public List<RegionTagDto> getAllRegionTags() {
        List<RegionTag> tags = regionTagRepository.findByDepth(0);
        List<RegionTagDto> tagListDto = tags.stream().map(RegionTagDto::new).collect(Collectors.toList());
        return tagListDto;
    }

    @Cacheable(value = "regionTag", key = "#tagName")
    public RegionTagDto getRegionTag(String tagName) throws BaseException {
        RegionTag tag = regionTagRepository.findByName(tagName).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_TAG)
        );
        return new RegionTagDto(tag);
    }

    public void changeRegionTag(ChangeRegionTagDto changeRegionTagDto) throws BaseException {
        Member member = memberRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(
                () -> new BaseException(BaseResponseStatus.UNAUTHORIZED)
        );
        RegionTag regionTag = regionTagRepository.findByName(changeRegionTagDto.getRegionName()).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_TAG)
        );
        member.changeRegion(regionTag);
    }
}
