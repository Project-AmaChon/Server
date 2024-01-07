package v1.amachon.tags.regionTag;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.tags.service.dto.RegionTagDto;
import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.repository.RegionTagRepository;
import v1.amachon.tags.service.RegionTagService;

import java.util.Optional;

@SpringBootTest
@Transactional
public class RegionTagServiceTest {

    @Autowired
    private RegionTagService regionTagService;

    @Autowired
    private RegionTagRepository regionTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CacheManager cacheManager;

    public static RegionTag 경기;

    @BeforeEach
    public void init() {
        경기 = RegionTag.builder().depth(0).name("경기").build();
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
    public void cachingTag() {
        RegionTagDto 용산구 = regionTagService.getRegionTag("용산구");

        Cache cache = cacheManager.getCache("regionTag");
        RegionTagDto cached = cache.get("용산구", RegionTagDto.class);
        Assertions.assertThat(용산구).isEqualTo(cached);
    }

    @Test
    @DisplayName("지역 태그 변경")
    public void changeRegionTag() {
        Member member = memberRepository.save(new Member("1", "1", "1"));
        Member member2 = memberRepository.save(new Member("2", "1", "1"));
        member.changeRegion(경기);
        member2.changeRegion(경기);
        Optional<Member> save = memberRepository.findByEmail("1");
        Optional<Member> save2 = memberRepository.findByEmail("2");
        Assertions.assertThat(member.getRegionTag()).isEqualTo(경기);
        Assertions.assertThat(member2.getRegionTag()).isEqualTo(경기);
    }
}
