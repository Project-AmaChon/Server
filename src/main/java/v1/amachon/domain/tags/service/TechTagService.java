package v1.amachon.domain.tags.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.TechTagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechTagService {

    private final TechTagRepository techTagRepository;

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
}
