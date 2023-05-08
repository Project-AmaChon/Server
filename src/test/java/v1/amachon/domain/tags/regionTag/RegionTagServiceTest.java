package v1.amachon.domain.tags.regionTag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.tags.dto.RegionTagDto;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.domain.tags.service.RegionTagService;

import java.util.List;

@SpringBootTest
public class RegionTagServiceTest {

    @Autowired
    private RegionTagService regionTagService;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void init() {
        RegionTag 경기 = RegionTag.builder().depth(0).name("경기").build();
        RegionTag 서울 = RegionTag.builder().depth(0).name("서울").build();
        RegionTag 인천 = RegionTag.builder().depth(0).name("인천").build();

        RegionTag 용산구 = RegionTag.builder().parent(서울).depth(1).name("용산구").build();
        RegionTag 은평구 = RegionTag.builder().parent(서울).depth(1).name("은평구").build();

        RegionTag 시흥시 = RegionTag.builder().parent(경기).depth(1).name("시흥시").build();
        RegionTag 안성시 = RegionTag.builder().parent(경기).depth(1).name("안성시").build();

        RegionTag 부평구 = RegionTag.builder().parent(인천).depth(1).name("부평구").build();
        RegionTag 연수구 = RegionTag.builder().parent(인천).depth(1).name("연수구").build();

        regionTagRepository.save(경기);
        regionTagRepository.save(서울);
        regionTagRepository.save(인천);

        regionTagRepository.save(용산구);
        regionTagRepository.save(은평구);

        regionTagRepository.save(시흥시);
        regionTagRepository.save(안성시);

        regionTagRepository.save(부평구);
        regionTagRepository.save(연수구);
    }

    @Test
    @DisplayName("지역 태그 캐싱")
    public void cachingTag() throws BaseException {
        RegionTagDto 용산구 = regionTagService.getRegionTag("용산구");

        Cache cache = cacheManager.getCache("regionTag");
        RegionTagDto cached = cache.get("용산구", RegionTagDto.class);
        Assertions.assertThat(용산구).isEqualTo(cached);
    }
}
