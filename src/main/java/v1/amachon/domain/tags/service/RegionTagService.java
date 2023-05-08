package v1.amachon.domain.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.tags.dto.RegionTagDto;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;

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
    public RegionTagDto getRegionTag(String tagName) throws BaseException {
        RegionTag tag = regionTagRepository.findByName(tagName).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INVALID_TAG)
        );
        return new RegionTagDto(tag);
    }
}
