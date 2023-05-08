package v1.amachon.domain.tags.techTag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.TechTagRepository;
import v1.amachon.domain.tags.service.TechTagService;

@SpringBootTest
public class TechTagServiceTest {

    @Autowired
    private TechTagService techTagService;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void init() {
        // parent
        TechTag backend = TechTag.builder().depth(0).name("Backend").build();
        TechTag frontend = TechTag.builder().depth(0).name("Frontend").build();
        techTagRepository.save(backend);
        techTagRepository.save(frontend);

        // child
        TechTag spring = TechTag.builder().depth(1).name("Spring").parent(backend).build();
        TechTag nodeJS = TechTag.builder().depth(1).name("NodeJS").parent(backend).build();
        TechTag reactJS = TechTag.builder().depth(1).name("ReactJS").parent(frontend).build();
        techTagRepository.save(spring);
        techTagRepository.save(nodeJS);
        techTagRepository.save(reactJS);
    }

    @Test
    @DisplayName("기술태그 캐싱")
    public void cachingTag() throws BaseException {
        // 처음 실행 시엔 캐시를 저장, 이미 실행했다면 캐시에서 가져옴
        TechTagDto spring = techTagService.getTechTag("Spring");

        Cache cache = cacheManager.getCache("techTag");
        TechTagDto cached = cache.get("Spring", TechTagDto.class);
        Assertions.assertThat(spring).isEqualTo(cached);
    }
}
