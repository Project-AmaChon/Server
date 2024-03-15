package v1.amachon.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.common.exception.UnauthorizedException;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.tags.service.dto.RegionTagDto;
import v1.amachon.tags.service.dto.change.ChangeRegionTagDto;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.service.exception.NotFoundRegionTagException;
import v1.amachon.common.config.security.util.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RegionTagService {

    private final RegionTagRepository regionTagRepository;

    @Cacheable(value = "regionTags")
    public List<RegionTagDto> getAllRegionTags() {
        List<RegionTag> tags = regionTagRepository.findByDepth(0);
        List<RegionTagDto> tagListDto = tags.stream().map(RegionTagDto::new).collect(Collectors.toList());
        return tagListDto;
    }

    @Cacheable(value = "regionTag", key = "#tagName")
    public List<String> getRegionTagNameWithChildrenTags(String tagName) {
        RegionTag tag = regionTagRepository.findByName(tagName)
                .orElseThrow(NotFoundRegionTagException::new);

        return tag.getTagNamesWithChildrenTags();
    }
}
