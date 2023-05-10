package v1.amachon.domain.tags.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.service.TechTagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagsController {

    private final TechTagService techTagService;

    @GetMapping("/tags")
    public BaseResponse<List<TechTagDto>> getTags() {
        techTagService.init();
        List<TechTagDto> tags = techTagService.getAllTechTags();
        return new BaseResponse<>(tags);
    }
}
