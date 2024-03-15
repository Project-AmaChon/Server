package v1.amachon.tags.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.tags.service.dto.RegionTagDto;
import v1.amachon.tags.service.dto.TechTagDto;
import v1.amachon.tags.service.dto.change.ChangeTagsDto;
import v1.amachon.tags.service.RegionTagService;
import v1.amachon.tags.service.TechTagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "태그 API")
public class TagsController {

    private final TechTagService techTagService;
    private final RegionTagService regionTagService;

    @ApiOperation(
            value = "전체 기술 태그 반환",
            notes = "기술 태그 선택을 위한 전체 태그를 반환"
    )
    @GetMapping("/tech-tags")
    public ResponseEntity<List<TechTagDto>> getTechTags() {
        return ResponseEntity.ok(techTagService.getAllTechTags());
    }

    @ApiOperation(
            value = "전체 지역 태그 반환",
            notes = "지역 태그 선택을 위한 전체 태그를 반환"
    )
    @GetMapping("/region-tags")
    public ResponseEntity<List<RegionTagDto>> getRegionTags() {
        return ResponseEntity.ok(regionTagService.getAllRegionTags());
    }

//    @ApiOperation(
//            value = "태그 선택(변경)",
//            notes = "지역 태그는 1개, 기술 태그는 3개 이하로 선택하여 태그를 설정"
//    )
//    @ApiResponses({
//            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
//            @ApiResponse(code = 2040, message = "태그가 올바르지 않습니다."),
//    })
//    @PostMapping("/change-tags")
//    public ResponseEntity<Void> changeTags(@RequestBody ChangeTagsDto changeTagsDto) {
//        techTagService.changeTechTags(changeTagsDto.getChangeTechTagDto());
//        regionTagService.changeRegionTag(changeTagsDto.getChangeRegionTagDto());
//        return ResponseEntity.noContent().build();
//    }
}
