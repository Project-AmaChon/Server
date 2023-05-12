package v1.amachon.domain.tags.techTag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.tags.dto.TechTagDto;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.MemberTechTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;
import v1.amachon.domain.tags.service.TechTagService;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@Transactional
public class TechTagServiceTest {

    @Autowired
    private TechTagService techTagService;

    @Autowired
    private TechTagRepository techTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private MemberTechTagRepository memberTechTagRepository;
    
    public static TechTag backend, frontend;
    
    @BeforeEach
    public void init() {
        // parent
        backend = TechTag.builder().depth(0).name("Backend").build();
        frontend = TechTag.builder().depth(0).name("Frontend").build();
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
    @DisplayName("기술 태그 캐싱")
    public void cachingTag() throws BaseException {
        // 처음 실행 시엔 캐시를 저장, 이미 실행했다면 캐시에서 가져옴
        TechTagDto spring = techTagService.getTechTag("Spring");

        Cache cache = cacheManager.getCache("techTag");
        TechTagDto cached = cache.get("Spring", TechTagDto.class);
        Assertions.assertThat(spring).isEqualTo(cached);
    }

    @Test
    @DisplayName("기술 태그 변경")
    public void changeRegionTag() throws BaseException {
        Member member = memberRepository.save(new Member("1", "1", "1"));
        MemberTechTag m = memberTechTagRepository.save(new MemberTechTag(member, backend));
        MemberTechTag f = memberTechTagRepository.save(new MemberTechTag(member, frontend));
        member.changeTechTag(Arrays.asList(m, f));
        Optional<Member> save = memberRepository.findByEmail("1");
        System.out.println("save.get().getTechTags() = " + save.get().getTechTags());
    }
}
