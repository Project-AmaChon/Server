package v1.amachon.domain.tags.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponse;
import v1.amachon.domain.tags.dto.RegionTagDto;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.dto.change.ChangeTagsDto;
import v1.amachon.domain.tags.service.RegionTagService;
import v1.amachon.domain.tags.service.TechTagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "태그 API")
public class TagsController {

    private final TechTagService techTagService;
    private final RegionTagService regionTagService;

    @ApiOperation(
            value = "전체 기술 태그 반환(임시)",
            notes = "기술 태그 선택을 위한 전체 태그를 반환"
    )
    @GetMapping("/tech-tags")
    public BaseResponse<List<TechTagDto>> getTechTags() {
        List<TechTagDto> tags = techTagService.getAllTechTags();
        return new BaseResponse<>(tags);
    }

    @ApiOperation(
            value = "전체 지역 태그 반환(임시)",
            notes = "지역 태그 선택을 위한 전체 태그를 반환"
    )
    @GetMapping("/region-tags")
    public BaseResponse<List<RegionTagDto>> getRegionTags() {
        List<RegionTagDto> tags = regionTagService.getAllRegionTags();
        return new BaseResponse<>(tags);
    }

    @ApiOperation(
            value = "태그 선택(변경)",
            notes = "지역 태그는 1개, 기술 태그는 3개 이하로 선택하여 태그를 설정"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2040, message = "올바르지 않은 태그명입니다."),
    })
    @PostMapping("/change-tags")
    public BaseResponse<String> changeTags(@RequestBody ChangeTagsDto changeTagsDto) throws BaseException {
        try {
            techTagService.changeTechTags(changeTagsDto.getChangeTechTagDto());
            regionTagService.changeRegionTag(changeTagsDto.getChangeRegionTagDto());
            return new BaseResponse<>("태그 변경 완료!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
