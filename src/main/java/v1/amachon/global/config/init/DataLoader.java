package v1.amachon.global.config.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.common.BaseEntity;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.member.service.dto.join.JoinDto;
import v1.amachon.domain.message.entity.MessageRoom;
import v1.amachon.domain.message.repository.MessageRoomRepository;
import v1.amachon.domain.message.service.MessageService;
import v1.amachon.domain.message.service.dto.SendMessageDto;
import v1.amachon.domain.project.entity.Project;
import v1.amachon.domain.project.entity.TeamMember;
import v1.amachon.domain.project.repository.ProjectRepository;
import v1.amachon.domain.project.repository.TeamMemberRepository;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;
import v1.amachon.domain.tags.entity.techtag.ProjectTechTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;
import v1.amachon.domain.tags.repository.MemberTechTagRepository;
import v1.amachon.domain.tags.repository.ProjectTechTagRepository;
import v1.amachon.domain.tags.repository.RegionTagRepository;
import v1.amachon.domain.tags.repository.TechTagRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final TechTagRepository techTagRepository;
    private final RegionTagRepository regionTagRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechTagRepository projectTechTagRepository;
    private final MemberRepository memberRepository;
    private final MemberTechTagRepository memberTechTagRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final MessageRoomRepository messageRoomRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // 기술 태그
        // 백엔드
        TechTag 백엔드 = techTagRepository.save(TechTag.builder().depth(0).name("백엔드").build());
        TechTag spring = techTagRepository.save(TechTag.builder().depth(1).name("Spring").parent(백엔드).build());
        TechTag nodejs = techTagRepository.save(TechTag.builder().depth(1).name("Nodejs").parent(백엔드).build());
        TechTag nestjs = techTagRepository.save(TechTag.builder().depth(1).name("Nestjs").parent(백엔드).build());
        TechTag go = techTagRepository.save(TechTag.builder().depth(1).name("Go").parent(백엔드).build());
        TechTag express = techTagRepository.save(TechTag.builder().depth(1).name("Express").parent(백엔드).build());
        TechTag mySQL = techTagRepository.save(TechTag.builder().depth(1).name("MySQL").parent(백엔드).build());
        TechTag mongoDB = techTagRepository.save(TechTag.builder().depth(1).name("MongoDB").parent(백엔드).build());
        TechTag django = techTagRepository.save(TechTag.builder().depth(1).name("Django").parent(백엔드).build());
        TechTag php = techTagRepository.save(TechTag.builder().depth(1).name("php").parent(백엔드).build());
        TechTag graphQL = techTagRepository.save(TechTag.builder().depth(1).name("GraphQL").parent(백엔드).build());
        TechTag firebase = techTagRepository.save(TechTag.builder().depth(1).name("Firebase").parent(백엔드).build());

        // 프론트엔드
        TechTag 프론트엔드 = techTagRepository.save(TechTag.builder().depth(0).name("프론트엔드").build());
        TechTag javaScript = techTagRepository.save(TechTag.builder().depth(1).name("JavaScript").parent(프론트엔드).build());
        TechTag typeScript = techTagRepository.save(TechTag.builder().depth(1).name("TypeScript").parent(프론트엔드).build());
        TechTag react = techTagRepository.save(TechTag.builder().depth(1).name("React").parent(프론트엔드).build());
        TechTag vue = techTagRepository.save(TechTag.builder().depth(1).name("Vue").parent(프론트엔드).build());
        TechTag svelte = techTagRepository.save(TechTag.builder().depth(1).name("Svelte").parent(프론트엔드).build());
        TechTag nextjs = techTagRepository.save(TechTag.builder().depth(1).name("Nextjs").parent(프론트엔드).build());

        // 모바일
        TechTag 모바일 = techTagRepository.save(TechTag.builder().depth(0).name("모바일").build());
        TechTag flutter = techTagRepository.save(TechTag.builder().depth(1).name("Flutter").parent(모바일).build());
        TechTag swift = techTagRepository.save(TechTag.builder().depth(1).name("Swift").parent(모바일).build());
        TechTag reactNative = techTagRepository.save(TechTag.builder().depth(1).name("ReactNative").parent(모바일).build());
        TechTag unity = techTagRepository.save(TechTag.builder().depth(1).name("Unity").parent(모바일).build());
        TechTag android = techTagRepository.save(TechTag.builder().depth(1).name("Android").parent(모바일).build());

        // 인프라
        TechTag 인프라 = techTagRepository.save(TechTag.builder().depth(0).name("인프라").build());
        TechTag aws = techTagRepository.save(TechTag.builder().depth(1).name("AWS").parent(인프라).build());
        TechTag kubernetes = techTagRepository.save(TechTag.builder().depth(1).name("Kubernetes").parent(인프라).build());
        TechTag docker = techTagRepository.save(TechTag.builder().depth(1).name("Docker").parent(인프라).build());

        // 기타
        TechTag 기타 = techTagRepository.save(TechTag.builder().depth(0).name("기타").build());
        TechTag git = techTagRepository.save(TechTag.builder().depth(1).name("Git").parent(기타).build());
        TechTag figma = techTagRepository.save(TechTag.builder().depth(1).name("Figma").parent(기타).build());
        TechTag zeplin = techTagRepository.save(TechTag.builder().depth(1).name("Zeplin").parent(기타).build());
        TechTag jest = techTagRepository.save(TechTag.builder().depth(1).name("Jest").parent(기타).build());

        // 지역 태그
        // 서울
        RegionTag 서울 = regionTagRepository.save(RegionTag.builder().depth(0).name("서울").build());
        RegionTag 강남구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("강남구").build());
        RegionTag 강동구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("강동구").build());
        RegionTag 강북구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("강북구").build());
        RegionTag 강서구_서울 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("강서구").build());
        RegionTag 관악구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("관악구").build());
        RegionTag 광진구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("광진구").build());
        RegionTag 구로구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("구로구").build());
        RegionTag 금천구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("금천구").build());
        RegionTag 노원구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("노원구").build());
        RegionTag 도봉구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("도봉구").build());
        RegionTag 동대문구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("동대문구").build());
        RegionTag 동작구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("동작구").build());
        RegionTag 마포구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("마포구").build());
        RegionTag 서대문구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("서대문구").build());
        RegionTag 서초구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("서초구").build());
        RegionTag 성동구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("성동구").build());
        RegionTag 성복구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("성복구").build());
        RegionTag 송파구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("송파구").build());
        RegionTag 양천구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("양천구").build());
        RegionTag 영등포구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("영등포구").build());
        RegionTag 용산구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("용산구").build());
        RegionTag 은평구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("은평구").build());
        RegionTag 종로구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("종로구").build());
        RegionTag 중구_서울 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("중구(서울)").build());
        RegionTag 중랑구 = regionTagRepository.save(RegionTag.builder().parent(서울).depth(1).name("중랑구").build());

        // 경기
        RegionTag 경기 = regionTagRepository.save(RegionTag.builder().depth(0).name("경기").build());
        RegionTag 가평군 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("가평군").build());
        RegionTag 고양시_덕양구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("고양시 덕양구").build());
        RegionTag 고양시_일산동구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("고양시 일산동구").build());
        RegionTag 고양시_일산서구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("고양시 일산서구").build());
        RegionTag 과천시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("과천시").build());
        RegionTag 광명시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("광명시").build());
        RegionTag 광주시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("광주시").build());
        RegionTag 구리시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("구리시").build());
        RegionTag 군포시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("군포시").build());
        RegionTag 김포시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("김포시").build());
        RegionTag 남양주시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("남양주시").build());
        RegionTag 동두천시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("동두천시").build());
        RegionTag 부천시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("부천시").build());
        RegionTag 성남시_분당구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("성남시 분당구").build());
        RegionTag 성남시_수정구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("성남시 수정구").build());
        RegionTag 성남시_중원구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("성남시 중원구").build());
        RegionTag 수원시_권선구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("수원시 권선구").build());
        RegionTag 수원시_영통구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("수원시 영통구").build());
        RegionTag 수원시_장안구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("수원시 장안구").build());
        RegionTag 수원시_팔달구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("수원시 팔달구").build());
        RegionTag 시흥시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("시흥시").build());
        RegionTag 안산시_단원구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("안산시 단원구").build());
        RegionTag 안산시_상록구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("안산시 상록구").build());
        RegionTag 안성시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("안성시").build());
        RegionTag 안양시_동안구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("안양시 동안구").build());
        RegionTag 안양시_만안구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("안양시 만안구").build());
        RegionTag 양주시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("양주시").build());
        RegionTag 양평군 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("양평군").build());
        RegionTag 여주시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("여주시").build());
        RegionTag 연천군 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("연천군").build());
        RegionTag 오산시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("오산시").build());
        RegionTag 용인시_기흥구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("용인시 기흥구").build());
        RegionTag 용인시_수지구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("용인시 수지구").build());
        RegionTag 용인시_처인구 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("용인시 처인구").build());
        RegionTag 의왕시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("의왕시").build());
        RegionTag 의정부시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("의정부시").build());
        RegionTag 이천시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("이천시").build());
        RegionTag 파주시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("파주시").build());
        RegionTag 평택시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("평택시").build());
        RegionTag 포천시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("포천시").build());
        RegionTag 하남시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("하남시").build());
        RegionTag 화성시 = regionTagRepository.save(RegionTag.builder().parent(경기).depth(1).name("화성시").build());

        // 인천
        RegionTag 인천 = regionTagRepository.save(RegionTag.builder().depth(0).name("인천").build());
        RegionTag 강화군 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("강화군").build());
        RegionTag 계양구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("계양구").build());
        RegionTag 남동구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("남동구").build());
        RegionTag 동구_인천 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("동구(인천)").build());
        RegionTag 미추홀구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("미추홀구").build());
        RegionTag 부평구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("부평구").build());
        RegionTag 서구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("서구(인천)").build());
        RegionTag 연수구 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("연수구").build());
        RegionTag 옹진군 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("옹진군").build());
        RegionTag 중구_인천 = regionTagRepository.save(RegionTag.builder().parent(인천).depth(1).name("중구(인천)").build());

        // 강원
        RegionTag 강원 = regionTagRepository.save(RegionTag.builder().depth(0).name("강원").build());
        RegionTag 강릉시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("강릉시").build());
        RegionTag 고성군_강원 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("고성군(강원)").build());
        RegionTag 동해시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("동해시").build());
        RegionTag 삼척시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("삼척시").build());
        RegionTag 속초시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("속초시").build());
        RegionTag 양구군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("양구군").build());
        RegionTag 양양군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("양양군").build());
        RegionTag 영월군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("영월군").build());
        RegionTag 원주시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("원주시").build());
        RegionTag 인제군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("인제군").build());
        RegionTag 정선군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("정선군").build());
        RegionTag 철원군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("철원군").build());
        RegionTag 춘천시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("춘천시").build());
        RegionTag 태백시 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("태백시").build());
        RegionTag 평창군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("평창군").build());
        RegionTag 홍천군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("홍천군").build());
        RegionTag 화천군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("화천군").build());
        RegionTag 횡성군 = regionTagRepository.save(RegionTag.builder().parent(강원).depth(1).name("횡성군").build());

        // 대전
        RegionTag 대전 = regionTagRepository.save(RegionTag.builder().depth(0).name("대전").build());
        RegionTag 대덕구 = regionTagRepository.save(RegionTag.builder().parent(대전).depth(1).name("대덕구").build());
        RegionTag 동구_대전 = regionTagRepository.save(RegionTag.builder().parent(대전).depth(1).name("동구(대전)").build());
        RegionTag 서구_대전 = regionTagRepository.save(RegionTag.builder().parent(대전).depth(1).name("서구(대전)").build());
        RegionTag 유성구 = regionTagRepository.save(RegionTag.builder().parent(대전).depth(1).name("유성구").build());
        RegionTag 중구_대전 = regionTagRepository.save(RegionTag.builder().parent(대전).depth(1).name("중구(대전)").build());

        // 세종
        RegionTag 세종 = regionTagRepository.save(RegionTag.builder().depth(0).name("세종").build());
        RegionTag 세종시 = regionTagRepository.save(RegionTag.builder().parent(세종).depth(1).name("세종시").build());

        // 충남
        RegionTag 충남 = regionTagRepository.save(RegionTag.builder().depth(0).name("충남").build());
        RegionTag 계룡시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("계룡시").build());
        RegionTag 공주시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("공주시").build());
        RegionTag 금산군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("금산군").build());
        RegionTag 논산시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("논산시").build());
        RegionTag 당진시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("당진시").build());
        RegionTag 보령시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("보령시").build());
        RegionTag 부여군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("부여군").build());
        RegionTag 서산시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("서산시").build());
        RegionTag 서천군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("서천군").build());
        RegionTag 아산시 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("아산시").build());
        RegionTag 예산군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("예산군").build());
        RegionTag 천안시_동남구 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("천안시 동남구").build());
        RegionTag 천안시_서북구 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("천안시 서북구").build());
        RegionTag 청양군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("청양군").build());
        RegionTag 태안군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("태안군").build());
        RegionTag 홍성군 = regionTagRepository.save(RegionTag.builder().parent(충남).depth(1).name("홍성군").build());

        // 충북
        RegionTag 충북 = regionTagRepository.save(RegionTag.builder().depth(0).name("충북").build());
        RegionTag 괴산군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("괴산군").build());
        RegionTag 단양군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("단양군").build());
        RegionTag 보은군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("보은군").build());
        RegionTag 영동군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("영동군").build());
        RegionTag 옥천군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("옥천군").build());
        RegionTag 음성군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("음성군").build());
        RegionTag 제천시 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("제천시").build());
        RegionTag 증평군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("증평군").build());
        RegionTag 진천군 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("진천군").build());
        RegionTag 청주시_상당구 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("청주시 상당구").build());
        RegionTag 청주시_서원구 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("청주시 서원구").build());
        RegionTag 청주시_청원구 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("청주시 청원구").build());
        RegionTag 청주시_흥덕구 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("청주시 흥덕구").build());
        RegionTag 충주시 = regionTagRepository.save(RegionTag.builder().parent(충북).depth(1).name("충주시").build());

        // 부산
        RegionTag 부산 = regionTagRepository.save(RegionTag.builder().depth(0).name("부산").build());
        RegionTag 강서구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("강서구(부산)").build());
        RegionTag 금정구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("금정구").build());
        RegionTag 기장군 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("기장군").build());
        RegionTag 남구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("남구(부산)").build());
        RegionTag 동구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("동구(부산)").build());
        RegionTag 동래구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("동래구").build());
        RegionTag 부산진구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("부산진구").build());
        RegionTag 북구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("북구(부산)").build());
        RegionTag 사상구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("사상구").build());
        RegionTag 사하구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("사하구").build());
        RegionTag 서구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("서구(부산)").build());
        RegionTag 수영구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("수영구").build());
        RegionTag 연제구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("연제구").build());
        RegionTag 영도구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("영도구").build());
        RegionTag 중구_부산 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("중구(부산)").build());
        RegionTag 해운대구 = regionTagRepository.save(RegionTag.builder().parent(부산).depth(1).name("해운대구").build());

        // 울산
        RegionTag 울산 = regionTagRepository.save(RegionTag.builder().depth(0).name("울산").build());
        RegionTag 남구_울산 = regionTagRepository.save(RegionTag.builder().parent(울산).depth(1).name("남구(울산)").build());
        RegionTag 동구_울산 = regionTagRepository.save(RegionTag.builder().parent(울산).depth(1).name("동구(울산)").build());
        RegionTag 북구_울산 = regionTagRepository.save(RegionTag.builder().parent(울산).depth(1).name("북구(울산)").build());
        RegionTag 울주군 = regionTagRepository.save(RegionTag.builder().parent(울산).depth(1).name("울주군").build());
        RegionTag 중구_울산 = regionTagRepository.save(RegionTag.builder().parent(울산).depth(1).name("중구(울산)").build());

        //경남
        RegionTag 경남 = regionTagRepository.save(RegionTag.builder().depth(0).name("경남").build());
        RegionTag 거제시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("거제시").build());
        RegionTag 거창군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("거창군").build());
        RegionTag 고성군_경남 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("고성군(경남)").build());
        RegionTag 김해시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("김해시").build());
        RegionTag 남해군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("남해군").build());
        RegionTag 밀양시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("밀양시").build());
        RegionTag 사천시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("사천시").build());
        RegionTag 산청군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("산청군").build());
        RegionTag 양산시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("양산시").build());
        RegionTag 의령군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("의령군").build());
        RegionTag 진주시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("진주시").build());
        RegionTag 창녕군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("창녕군").build());
        RegionTag 창원시_마산합포구 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("창원시 마산합포구").build());
        RegionTag 창원시_마산회원구 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("창원시 마산회원구").build());
        RegionTag 창원시_성산구 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("창원시 성산구").build());
        RegionTag 창원시_진해구 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("창원시 진해구").build());
        RegionTag 통영시 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("통영시").build());
        RegionTag 하동군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("하동군").build());
        RegionTag 함안군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("함안군").build());
        RegionTag 함양군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("함양군").build());
        RegionTag 합천군 = regionTagRepository.save(RegionTag.builder().parent(경남).depth(1).name("합천군").build());

        // 경북
        RegionTag 경북 = regionTagRepository.save(RegionTag.builder().depth(0).name("경북").build());
        RegionTag 경산시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("경산시").build());
        RegionTag 경주시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("경주시").build());
        RegionTag 고령군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("고령군").build());
        RegionTag 구미시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("구미시").build());
        RegionTag 군위군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("군위군").build());
        RegionTag 김천시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("김천시").build());
        RegionTag 문경시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("문경시").build());
        RegionTag 봉화군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("봉화군").build());
        RegionTag 상주시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("상주시").build());
        RegionTag 성주군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("성주군").build());
        RegionTag 안동시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("안동시").build());
        RegionTag 영덕군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("영덕군").build());
        RegionTag 영양군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("영양군").build());
        RegionTag 영주시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("영주시").build());
        RegionTag 영천시 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("영천시").build());
        RegionTag 예천군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("예천군").build());
        RegionTag 울릉군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("울릉군").build());
        RegionTag 울진군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("울진군").build());
        RegionTag 의성군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("의성군").build());
        RegionTag 청도군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("청도군").build());
        RegionTag 청송군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("청송군").build());
        RegionTag 칠곡군 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("칠곡군").build());
        RegionTag 포항시_남구 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("포항시 남구").build());
        RegionTag 포항시_북구 = regionTagRepository.save(RegionTag.builder().parent(경북).depth(1).name("포항시 북구").build());

        // 대구
        RegionTag 대구 = regionTagRepository.save(RegionTag.builder().depth(0).name("대구").build());
        RegionTag 남구_대구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("남구(대구)").build());
        RegionTag 달서구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("달서구").build());
        RegionTag 달성군 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("달성군").build());
        RegionTag 동구_대구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("동구(대구)").build());
        RegionTag 북구_대구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("북구(대구)").build());
        RegionTag 서구_대구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("서구(대구)").build());
        RegionTag 수성구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("수성구").build());
        RegionTag 중구_대구 = regionTagRepository.save(RegionTag.builder().parent(대구).depth(1).name("중구(대구)").build());

        // 광주
        RegionTag 광주 = regionTagRepository.save(RegionTag.builder().depth(0).name("광주").build());
        RegionTag 광산구 = regionTagRepository.save(RegionTag.builder().parent(광주).depth(1).name("광산구").build());
        RegionTag 남구_광주 = regionTagRepository.save(RegionTag.builder().parent(광주).depth(1).name("남구(광주)").build());
        RegionTag 동구_광주 = regionTagRepository.save(RegionTag.builder().parent(광주).depth(1).name("동구(광주)").build());
        RegionTag 북구_광주 = regionTagRepository.save(RegionTag.builder().parent(광주).depth(1).name("북구(광주)").build());
        RegionTag 서구_광주 = regionTagRepository.save(RegionTag.builder().parent(광주).depth(1).name("서구(광주)").build());

        // 전남
        RegionTag 전남 = regionTagRepository.save(RegionTag.builder().depth(0).name("전남").build());
        RegionTag 강진군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("강진군").build());
        RegionTag 고흥군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("고흥군").build());
        RegionTag 곡성군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("곡성군").build());
        RegionTag 광양시 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("광양시").build());
        RegionTag 구례군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("구례군").build());
        RegionTag 나주시 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("나주시").build());
        RegionTag 담양군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("담양군").build());
        RegionTag 목포시 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("목포시").build());
        RegionTag 무안군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("무안군").build());
        RegionTag 보성군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("보성군").build());
        RegionTag 순천시 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("순천시").build());
        RegionTag 신안군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("신안군").build());
        RegionTag 여수시 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("여수시").build());
        RegionTag 영광군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("영광군").build());
        RegionTag 영암군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("영암군").build());
        RegionTag 완도군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("완도군").build());
        RegionTag 장성군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("장성군").build());
        RegionTag 장흥군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("장흥군").build());
        RegionTag 진도군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("진도군").build());
        RegionTag 함평군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("함평군").build());
        RegionTag 해남군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("해남군").build());
        RegionTag 화순군 = regionTagRepository.save(RegionTag.builder().parent(전남).depth(1).name("화순군").build());

        // 전북
        RegionTag 전북 = regionTagRepository.save(RegionTag.builder().depth(0).name("전북").build());
        RegionTag 고창군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("고창군").build());
        RegionTag 군산시 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("군산시").build());
        RegionTag 김제시 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("김제시").build());
        RegionTag 남원시 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("남원시").build());
        RegionTag 무주군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("무주군").build());
        RegionTag 부안군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("부안군").build());
        RegionTag 순창군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("순창군").build());
        RegionTag 완주군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("완주군").build());
        RegionTag 익산시 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("익산시").build());
        RegionTag 임실군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("임실군").build());
        RegionTag 장수군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("장수군").build());
        RegionTag 전주시_덕진구 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("전주시 덕진구").build());
        RegionTag 전주시_완산구 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("전주시 완산구").build());
        RegionTag 정읍시 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("정읍시").build());
        RegionTag 진안군 = regionTagRepository.save(RegionTag.builder().parent(전북).depth(1).name("진안군").build());

        // 제주
        RegionTag 제주 = regionTagRepository.save(RegionTag.builder().depth(0).name("제주").build());
        RegionTag 서귀포시 = regionTagRepository.save(RegionTag.builder().parent(제주).depth(1).name("서귀포시").build());
        RegionTag 제주시 = regionTagRepository.save(RegionTag.builder().parent(제주).depth(1).name("제주시").build());


        // 멤버
        Member 이정우 = Member.ofMember(JoinDto.builder().email("member1@naver.com").nickname("이정우").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 박종범 = Member.ofMember(JoinDto.builder().email("member2@naver.com").nickname("박종범").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 전재욱 = Member.ofMember(JoinDto.builder().email("member3@naver.com").nickname("전재욱").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 전승현 = Member.ofMember(JoinDto.builder().email("member4@naver.com").nickname("전승현").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 허규범 = Member.ofMember(JoinDto.builder().email("member5@naver.com").nickname("허규범").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 카리나 = Member.ofMember(JoinDto.builder().email("member6@naver.com").nickname("카리나").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 윈터 = Member.ofMember(JoinDto.builder().email("member7@naver.com").nickname("윈터").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 닝닝 = Member.ofMember(JoinDto.builder().email("member8@naver.com").nickname("닝닝").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 지젤 = Member.ofMember(JoinDto.builder().email("member9@naver.com").nickname("지젤").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 카즈하 = Member.ofMember(JoinDto.builder().email("member10@naver.com").nickname("카즈하").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 김채원 = Member.ofMember(JoinDto.builder().email("member11@naver.com").nickname("김채원").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 허윤진 = Member.ofMember(JoinDto.builder().email("member12@naver.com").nickname("허윤진").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 사쿠라 = Member.ofMember(JoinDto.builder().email("member13@naver.com").nickname("사쿠라").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 홍은채 = Member.ofMember(JoinDto.builder().email("member14@naver.com").nickname("홍은채").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 장원영 = Member.ofMember(JoinDto.builder().email("member15@naver.com").nickname("장원영").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 레이 = Member.ofMember(JoinDto.builder().email("member16@naver.com").nickname("레이").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 안유진 = Member.ofMember(JoinDto.builder().email("member17@naver.com").nickname("안유진").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 이서 = Member.ofMember(JoinDto.builder().email("member18@naver.com").nickname("이서").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 가을 = Member.ofMember(JoinDto.builder().email("member19@naver.com").nickname("가을").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 리즈 = Member.ofMember(JoinDto.builder().email("member20@naver.com").nickname("리즈").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 하니 = Member.ofMember(JoinDto.builder().email("member21@naver.com").nickname("하니").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 민지 = Member.ofMember(JoinDto.builder().email("member22@naver.com").nickname("민지").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 해린 = Member.ofMember(JoinDto.builder().email("member23@naver.com").nickname("해린").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 다니엘 = Member.ofMember(JoinDto.builder().email("member24@naver.com").nickname("다니엘").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 혜인 = Member.ofMember(JoinDto.builder().email("member25@naver.com").nickname("혜인").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 제니 = Member.ofMember(JoinDto.builder().email("member26@naver.com").nickname("제니").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 리사 = Member.ofMember(JoinDto.builder().email("member27@naver.com").nickname("리사").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 지수 = Member.ofMember(JoinDto.builder().email("member28@naver.com").nickname("지수").password(passwordEncoder.encode("wjddn6138!")).build());
        Member 로제 = Member.ofMember(JoinDto.builder().email("member29@naver.com").nickname("로제").password(passwordEncoder.encode("wjddn6138!")).build());

        memberRepository.save(이정우);
        memberRepository.save(박종범);
        memberRepository.save(전재욱);
        memberRepository.save(전승현);
        memberRepository.save(허규범);
        memberRepository.save(카리나);
        memberRepository.save(윈터);
        memberRepository.save(닝닝);
        memberRepository.save(지젤);
        memberRepository.save(카즈하);
        memberRepository.save(김채원);
        memberRepository.save(허윤진);
        memberRepository.save(사쿠라);
        memberRepository.save(홍은채);
        memberRepository.save(장원영);
        memberRepository.save(레이);
        memberRepository.save(안유진);
        memberRepository.save(이서);
        memberRepository.save(가을);
        memberRepository.save(리즈);
        memberRepository.save(하니);
        memberRepository.save(민지);
        memberRepository.save(해린);
        memberRepository.save(다니엘);
        memberRepository.save(혜인);
        memberRepository.save(제니);
        memberRepository.save(리사);
        memberRepository.save(지수);
        memberRepository.save(로제);

        MemberTechTag 이정우1 = memberTechTagRepository.save(new MemberTechTag(이정우, spring));
        MemberTechTag 이정우2 = memberTechTagRepository.save(new MemberTechTag(이정우, android));
        MemberTechTag 이정우3 = memberTechTagRepository.save(new MemberTechTag(이정우, mySQL));
        MemberTechTag 박종범1 = memberTechTagRepository.save(new MemberTechTag(박종범, reactNative));
        MemberTechTag 박종범2 = memberTechTagRepository.save(new MemberTechTag(박종범, react));
        MemberTechTag 박종범3 = memberTechTagRepository.save(new MemberTechTag(박종범, android));
        MemberTechTag 전재욱1 = memberTechTagRepository.save(new MemberTechTag(전재욱, spring));
        MemberTechTag 전재욱2 = memberTechTagRepository.save(new MemberTechTag(전재욱, git));
        MemberTechTag 전재욱3 = memberTechTagRepository.save(new MemberTechTag(전재욱, aws));
        MemberTechTag 전승현1 = memberTechTagRepository.save(new MemberTechTag(전승현, android));
        MemberTechTag 전승현2 = memberTechTagRepository.save(new MemberTechTag(전승현, django));
        MemberTechTag 전승현3 = memberTechTagRepository.save(new MemberTechTag(전승현, docker));
        MemberTechTag 허규범1 = memberTechTagRepository.save(new MemberTechTag(허규범, unity));
        MemberTechTag 허규범2 = memberTechTagRepository.save(new MemberTechTag(허규범, android));
        MemberTechTag 허규범3 = memberTechTagRepository.save(new MemberTechTag(허규범, figma));
        MemberTechTag 카리나1 = memberTechTagRepository.save(new MemberTechTag(카리나, react));
        MemberTechTag 카리나2 = memberTechTagRepository.save(new MemberTechTag(카리나, figma));
        MemberTechTag 카리나3 = memberTechTagRepository.save(new MemberTechTag(카리나, android));
        MemberTechTag 윈터1 = memberTechTagRepository.save(new MemberTechTag(윈터, spring));
        MemberTechTag 윈터2 = memberTechTagRepository.save(new MemberTechTag(윈터, mySQL));
        MemberTechTag 윈터3 = memberTechTagRepository.save(new MemberTechTag(윈터, aws));
        MemberTechTag 닝닝1 = memberTechTagRepository.save(new MemberTechTag(닝닝, flutter));
        MemberTechTag 닝닝2 = memberTechTagRepository.save(new MemberTechTag(닝닝, javaScript));
        MemberTechTag 닝닝3 = memberTechTagRepository.save(new MemberTechTag(닝닝, git));
        MemberTechTag 지젤1 = memberTechTagRepository.save(new MemberTechTag(지젤, typeScript));
        MemberTechTag 지젤2 = memberTechTagRepository.save(new MemberTechTag(지젤, javaScript));
        MemberTechTag 지젤3 = memberTechTagRepository.save(new MemberTechTag(지젤, nextjs));
        MemberTechTag 카즈하1 = memberTechTagRepository.save(new MemberTechTag(카즈하, figma));
        MemberTechTag 카즈하2 = memberTechTagRepository.save(new MemberTechTag(카즈하, git));
        MemberTechTag 카즈하3 = memberTechTagRepository.save(new MemberTechTag(카즈하, docker));
        MemberTechTag 김채원1 = memberTechTagRepository.save(new MemberTechTag(김채원, vue));
        MemberTechTag 김채원2 = memberTechTagRepository.save(new MemberTechTag(김채원, spring));
        MemberTechTag 김채원3 = memberTechTagRepository.save(new MemberTechTag(김채원, git));
        MemberTechTag 허윤진1 = memberTechTagRepository.save(new MemberTechTag(허윤진, spring));
        MemberTechTag 허윤진2 = memberTechTagRepository.save(new MemberTechTag(허윤진, docker));
        MemberTechTag 허윤진3 = memberTechTagRepository.save(new MemberTechTag(허윤진, react));
        MemberTechTag 사쿠라1 = memberTechTagRepository.save(new MemberTechTag(사쿠라, nodejs));
        MemberTechTag 사쿠라2 = memberTechTagRepository.save(new MemberTechTag(사쿠라, nestjs));
        MemberTechTag 사쿠라3 = memberTechTagRepository.save(new MemberTechTag(사쿠라, php));
        MemberTechTag 홍은채1 = memberTechTagRepository.save(new MemberTechTag(홍은채, go));
        MemberTechTag 홍은채2 = memberTechTagRepository.save(new MemberTechTag(홍은채, mongoDB));
        MemberTechTag 홍은채3 = memberTechTagRepository.save(new MemberTechTag(홍은채, swift));
        MemberTechTag 장원영1 = memberTechTagRepository.save(new MemberTechTag(장원영, flutter));
        MemberTechTag 장원영2 = memberTechTagRepository.save(new MemberTechTag(장원영, swift));
        MemberTechTag 장원영3 = memberTechTagRepository.save(new MemberTechTag(장원영, reactNative));
        MemberTechTag 레이1 = memberTechTagRepository.save(new MemberTechTag(레이, figma));
        MemberTechTag 레이2 = memberTechTagRepository.save(new MemberTechTag(레이, unity));
        MemberTechTag 레이3 = memberTechTagRepository.save(new MemberTechTag(레이, android));
        MemberTechTag 안유진1 = memberTechTagRepository.save(new MemberTechTag(안유진, nodejs));
        MemberTechTag 안유진2 = memberTechTagRepository.save(new MemberTechTag(안유진, spring));
        MemberTechTag 안유진3 = memberTechTagRepository.save(new MemberTechTag(안유진, aws));
        MemberTechTag 이서1 = memberTechTagRepository.save(new MemberTechTag(이서, flutter));
        MemberTechTag 이서2 = memberTechTagRepository.save(new MemberTechTag(이서, firebase));
        MemberTechTag 이서3 = memberTechTagRepository.save(new MemberTechTag(이서, svelte));
        MemberTechTag 가을1 = memberTechTagRepository.save(new MemberTechTag(가을, go));
        MemberTechTag 가을2 = memberTechTagRepository.save(new MemberTechTag(가을, express));
        MemberTechTag 가을3 = memberTechTagRepository.save(new MemberTechTag(가을, nodejs));
        MemberTechTag 리즈1 = memberTechTagRepository.save(new MemberTechTag(리즈, unity));
        MemberTechTag 리즈2 = memberTechTagRepository.save(new MemberTechTag(리즈, kubernetes));
        MemberTechTag 리즈3 = memberTechTagRepository.save(new MemberTechTag(리즈, docker));
        MemberTechTag 하니1 = memberTechTagRepository.save(new MemberTechTag(하니, figma));
        MemberTechTag 하니2 = memberTechTagRepository.save(new MemberTechTag(하니, firebase));
        MemberTechTag 하니3 = memberTechTagRepository.save(new MemberTechTag(하니, git));
        MemberTechTag 민지1 = memberTechTagRepository.save(new MemberTechTag(민지, react));
        MemberTechTag 민지2 = memberTechTagRepository.save(new MemberTechTag(민지, reactNative));
        MemberTechTag 민지3 = memberTechTagRepository.save(new MemberTechTag(민지, git));
        MemberTechTag 해린1 = memberTechTagRepository.save(new MemberTechTag(해린, spring));
        MemberTechTag 해린2 = memberTechTagRepository.save(new MemberTechTag(해린, android));
        MemberTechTag 해린3 = memberTechTagRepository.save(new MemberTechTag(해린, mySQL));
        MemberTechTag 다니엘1 = memberTechTagRepository.save(new MemberTechTag(다니엘, spring));
        MemberTechTag 다니엘2 = memberTechTagRepository.save(new MemberTechTag(다니엘, android));
        MemberTechTag 다니엘3 = memberTechTagRepository.save(new MemberTechTag(다니엘, reactNative));
        MemberTechTag 혜인1 = memberTechTagRepository.save(new MemberTechTag(혜인, figma));
        MemberTechTag 혜인2 = memberTechTagRepository.save(new MemberTechTag(혜인, zeplin));
        MemberTechTag 혜인3 = memberTechTagRepository.save(new MemberTechTag(혜인, jest));
        MemberTechTag 제니1 = memberTechTagRepository.save(new MemberTechTag(제니, react));
        MemberTechTag 제니2 = memberTechTagRepository.save(new MemberTechTag(제니, android));
        MemberTechTag 제니3 = memberTechTagRepository.save(new MemberTechTag(제니, git));
        MemberTechTag 리사1 = memberTechTagRepository.save(new MemberTechTag(리사, kubernetes));
        MemberTechTag 리사2 = memberTechTagRepository.save(new MemberTechTag(리사, figma));
        MemberTechTag 리사3 = memberTechTagRepository.save(new MemberTechTag(리사, git));
        MemberTechTag 지수1 = memberTechTagRepository.save(new MemberTechTag(지수, figma));
        MemberTechTag 지수2 = memberTechTagRepository.save(new MemberTechTag(지수, react));
        MemberTechTag 지수3 = memberTechTagRepository.save(new MemberTechTag(지수, git));
        MemberTechTag 로제1 = memberTechTagRepository.save(new MemberTechTag(로제, spring));
        MemberTechTag 로제2 = memberTechTagRepository.save(new MemberTechTag(로제, git));
        MemberTechTag 로제3 = memberTechTagRepository.save(new MemberTechTag(로제, aws));


        이정우.changeRegion(화성시);
        이정우.changeTechTag(Arrays.asList(이정우1, 이정우2, 이정우3));
        박종범.changeRegion(용인시_처인구);
        박종범.changeTechTag(Arrays.asList(박종범1, 박종범2, 박종범3));
        전재욱.changeRegion(의정부시);
        전재욱.changeTechTag(Arrays.asList(전재욱1, 전재욱2, 전재욱3));
        전승현.changeRegion(성남시_분당구);
        전승현.changeTechTag(Arrays.asList(전승현1, 전승현2, 전승현3));
        허규범.changeRegion(수원시_영통구);
        허규범.changeTechTag(Arrays.asList(허규범1, 허규범2, 허규범3));
        카리나.changeRegion(화성시);
        카리나.changeTechTag(Arrays.asList(카리나1, 카리나2, 카리나3));
        윈터.changeRegion(화성시);
        윈터.changeTechTag(Arrays.asList(윈터1, 윈터2, 윈터3));
        닝닝.changeRegion(용인시_처인구);
        닝닝.changeTechTag(Arrays.asList(닝닝1, 닝닝2, 닝닝3));
        지젤.changeRegion(용인시_처인구);
        지젤.changeTechTag(Arrays.asList(지젤1, 지젤2, 지젤3));
        카즈하.changeRegion(수원시_영통구);
        카즈하.changeTechTag(Arrays.asList(카즈하1, 카즈하2, 카즈하3));
        김채원.changeRegion(의정부시);
        김채원.changeTechTag(Arrays.asList(김채원1, 김채원2, 김채원3));
        허윤진.changeRegion(화성시);
        허윤진.changeTechTag(Arrays.asList(허윤진1, 허윤진2, 허윤진3));
        사쿠라.changeRegion(성남시_분당구);
        사쿠라.changeTechTag(Arrays.asList(사쿠라1, 사쿠라2, 사쿠라3));
        홍은채.changeRegion(의정부시);
        홍은채.changeTechTag(Arrays.asList(홍은채1, 홍은채2, 홍은채3));
        장원영.changeRegion(화성시);
        장원영.changeTechTag(Arrays.asList(장원영1, 장원영2, 장원영3));
        레이.changeRegion(화성시);
        레이.changeTechTag(Arrays.asList(레이1, 레이2, 레이3));
        안유진.changeRegion(성남시_분당구);
        안유진.changeTechTag(Arrays.asList(안유진1, 안유진2, 안유진3));
        이서.changeRegion(수원시_영통구);
        이서.changeTechTag(Arrays.asList(이서1, 이서2, 이서3));
        가을.changeRegion(강남구);
        가을.changeTechTag(Arrays.asList(가을1, 가을2, 가을3));
        리즈.changeRegion(해운대구);
        리즈.changeTechTag(Arrays.asList(리즈1, 리즈2, 리즈3));
        하니.changeRegion(인제군);
        하니.changeTechTag(Arrays.asList(하니1, 하니2, 하니3));
        민지.changeRegion(화성시);
        민지.changeTechTag(Arrays.asList(민지1, 민지2, 민지3));
        해린.changeRegion(화성시);
        해린.changeTechTag(Arrays.asList(해린1, 해린2, 해린3));
        다니엘.changeRegion(강동구);
        다니엘.changeTechTag(Arrays.asList(다니엘1, 다니엘2, 다니엘3));
        혜인.changeRegion(광주시);
        혜인.changeTechTag(Arrays.asList(혜인1, 혜인2, 혜인3));
        제니.changeRegion(강남구);
        제니.changeTechTag(Arrays.asList(제니1, 제니2, 제니3));
        리사.changeRegion(영등포구);
        리사.changeTechTag(Arrays.asList(리사1, 리사2, 리사3));
        지수.changeRegion(의정부시);
        지수.changeTechTag(Arrays.asList(지수1, 지수2, 지수3));
        로제.changeRegion(영등포구);
        로제.changeTechTag(Arrays.asList(로제1, 로제2, 로제3));

        memberRepository.save(이정우);
        memberRepository.save(박종범);
        memberRepository.save(전재욱);
        memberRepository.save(전승현);
        memberRepository.save(허규범);
        memberRepository.save(카리나);
        memberRepository.save(윈터);
        memberRepository.save(닝닝);
        memberRepository.save(지젤);
        memberRepository.save(카즈하);
        memberRepository.save(김채원);
        memberRepository.save(허윤진);
        memberRepository.save(사쿠라);
        memberRepository.save(홍은채);
        memberRepository.save(장원영);
        memberRepository.save(레이);
        memberRepository.save(안유진);
        memberRepository.save(이서);
        memberRepository.save(가을);
        memberRepository.save(리즈);
        memberRepository.save(하니);
        memberRepository.save(민지);
        memberRepository.save(해린);
        memberRepository.save(다니엘);
        memberRepository.save(혜인);
        memberRepository.save(제니);
        memberRepository.save(리사);
        memberRepository.save(지수);
        memberRepository.save(로제);

        // 프로필
//        이정우.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 이정우입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/b8c337b1-5d9a-4671-9d63-34b139169aaf_%EC%9D%B4%EC%A0%95%EC%9A%B0.jpg", "개발을 좋아합니다", "깃허브", "블로그",이정우.getNickname(), 이정우.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 이정우.getRegionTag().getName()));
//        전재욱.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 전재욱입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/3c40ce6e-4ea9-4471-9446-0c47017e5d0b_%EC%9E%AC%EC%9A%B1.jpg", "개발을 좋아합니다", "깃허브", "블로그", 전재욱.getNickname(), 전재욱.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 전재욱.getRegionTag().getName()));
//        박종범.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 박종범입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/5fb6cad7-794f-4a6b-a694-0e1b8aca3197_%EC%A2%85%EB%B2%942.jpg", "개발을 좋아합니다", "깃허브", "블로그", 박종범.getNickname(), 박종범.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 박종범.getRegionTag().getName()));
//        전승현.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 전승현입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/4477492e-81dd-4a7f-ad17-63f9c257cdff_%EC%8A%B9%ED%98%84.jpg", "개발을 좋아합니다", "깃허브", "블로그", 전승현.getNickname(), 전승현.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 전승현.getRegionTag().getName()));
//        허규범.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 허규범입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/8c764d1e-b6b8-472e-be91-d6a01a75a429_%EA%B7%9C%EB%B2%94.jpg", "개발을 좋아합니다", "깃허브", "블로그", 허규범.getNickname(), 허규범.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 허규범.getRegionTag().getName()));
//        카리나.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 카리나입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/326ff989-e2a6-46da-9c1f-afdaefd06ce7_%EC%B9%B4%EB%A6%AC%EB%82%98.jpg", "개발을 좋아합니다", "깃허브", "블로그", 카리나.getNickname(), 카리나.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 카리나.getRegionTag().getName()));
//        윈터.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 윈터입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/b9b4c977-e73b-460d-941b-ecde85f260e7_%EC%9C%88%ED%84%B0.jpg", "개발을 좋아합니다", "깃허브", "블로그", 윈터.getNickname(), 윈터.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 윈터.getRegionTag().getName()));
//        닝닝.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 닝닝입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/b57cb12a-8096-4ed2-b279-a507f15f99f0_%EB%8B%9D%EB%8B%9D.jpg", "개발을 좋아합니다", "깃허브", "블로그", 닝닝.getNickname(), 닝닝.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 닝닝.getRegionTag().getName()));
//        지젤.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 지젤입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/abeb7655-b0bb-467f-a5e4-de0a5ae9dbab_%EC%A7%80%EC%A0%A4.jpg", "개발을 좋아합니다", "깃허브", "블로그", 지젤.getNickname(), 지젤.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 지젤.getRegionTag().getName()));
//        카즈하.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 카즈하입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/e9720319-45b8-4197-81c1-b3dc34f11b91_%EC%B9%B4%EC%A6%88%ED%95%98.jpg", "개발을 좋아합니다", "깃허브", "블로그", 카즈하.getNickname(), 카즈하.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 카즈하.getRegionTag().getName()));
//        김채원.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 김채원입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/ed39dbe8-cecd-4f87-be06-31fee4937050_%EA%B9%80%EC%B1%84%EC%9B%90.jpg", "개발을 좋아합니다", "깃허브", "블로그", 김채원.getNickname(), 김채원.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 김채원.getRegionTag().getName()));
//        허윤진.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 허윤진입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/1a120bd0-e5a7-436c-961c-f02eda810f45_%ED%97%88%EC%9C%A4%EC%A7%84.jpg", "개발을 좋아합니다", "깃허브", "블로그", 허윤진.getNickname(),허윤진.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 허윤진.getRegionTag().getName()));
//        사쿠라.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 사쿠라입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/33d59aa5-0996-4918-a2cd-27d0fb62f03a_%EC%82%AC%EC%BF%A0%EB%9D%BC.jpg", "개발을 좋아합니다", "깃허브", "블로그", 사쿠라.getNickname(), 사쿠라.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 사쿠라.getRegionTag().getName()));
//        홍은채.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 홍은채입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/5876b907-17aa-4d20-b71a-9491c6439ce5_%ED%99%8D%EC%9D%80%EC%B1%84.jpg", "개발을 좋아합니다", "깃허브", "블로그", 홍은채.getNickname(), 홍은채.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 홍은채.getRegionTag().getName()));
//        장원영.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 장원영입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/933975b4-d273-479c-b99f-062fb9c4fa49_%EC%9E%A5%EC%9B%90%EC%98%81.jpg", "개발을 좋아합니다", "깃허브", "블로그", 장원영.getNickname(), 장원영.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 장원영.getRegionTag().getName()));
//        레이.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 레이입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/bd6bde35-4f79-4699-bfae-5e557bcfcc70_%EB%A0%88%EC%9D%B4.jpg", "개발을 좋아합니다", "깃허브", "블로그", 레이.getNickname(), 레이.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 레이.getRegionTag().getName()));
//        안유진.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 안유진입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/ef3b7b1d-f5e0-4b08-bffe-ba3b855cfc67_%EC%95%88%EC%9C%A0%EC%A7%84.jpg", "개발을 좋아합니다", "깃허브", "블로그", 안유진.getNickname(),안유진.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 안유진.getRegionTag().getName()));
//        이서.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 이서입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/0921bda2-9eff-40d8-a770-5306befe24c0_%EC%9D%B4%EC%84%9C.jpg", "개발을 좋아합니다", "깃허브", "블로그", 이서.getNickname(), 이서.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 이서.getRegionTag().getName()));
//        가을.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 가을입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/a468dfda-7dce-447f-97c3-3e052d77e7ff_%EA%B0%80%EC%9D%84.jpg", "개발을 좋아합니다", "깃허브", "블로그", 가을.getNickname(), 가을.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 가을.getRegionTag().getName()));
//        리즈.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 리즈입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/387e4f4b-a52b-411d-85d4-ea39d3ef2890_%EB%A6%AC%EC%A6%88.jpg", "개발을 좋아합니다", "깃허브", "블로그", 리즈.getNickname(), 리즈.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 리즈.getRegionTag().getName()));
//        하니.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 하니입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/67bd6685-e105-49f6-bc0a-da84130e454a_%ED%95%98%EB%8B%88.jpeg", "개발을 좋아합니다", "깃허브", "블로그", 하니.getNickname(), 하니.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 하니.getRegionTag().getName()));
//        민지.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 민지입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/1b9e04e2-b3b9-4967-ae86-2eea2dc8dcf0_%EB%AF%BC%EC%A7%80.jpg", "개발을 좋아합니다", "깃허브", "블로그", 민지.getNickname(), 민지.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 민지.getRegionTag().getName()));
//        해린.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 해린입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/a41167fb-7050-425e-8723-7e89935c86c5_%ED%95%B4%EB%A6%B0.jpg", "개발을 좋아합니다", "깃허브", "블로그", 해린.getNickname(), 해린.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 해린.getRegionTag().getName()));
//        다니엘.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 다니엘입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/c1a3cbc5-b5d7-4a8c-90ba-a4575b0759f8_%EB%8B%A4%EB%8B%88%EC%97%98.jpg", "설명", "깃허브", "블로그", 다니엘.getNickname(), 다니엘.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 다니엘.getRegionTag().getName()));
//        혜인.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 혜인입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/bd38c4ed-cc4a-4ebe-8060-062de60f06fa_%ED%98%9C%EC%9D%B8.png", "개발을 좋아합니다", "깃허브", "블로그", 혜인.getNickname(), 혜인.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 혜인.getRegionTag().getName()));
//        제니.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 제니입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/e5b69f38-c2f9-4f4d-9401-ef455fb59d84_%EC%A0%9C%EB%8B%88.png", "개발을 좋아합니다", "깃허브", "블로그", 제니.getNickname(), 제니.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 제니.getRegionTag().getName()));
//        리사.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 리사입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/3253f752-e4cb-44a6-ad1d-e4552d7243f5_%EB%A6%AC%EC%82%AC.png", "개발을 좋아합니다", "깃허브", "블로그", 리사.getNickname(), 리사.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 리사.getRegionTag().getName()));
//        지수.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 지수입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/00086608-3eab-4615-8efa-2435c5261d97_%EC%A7%80%EC%88%98.png", "개발을 좋아합니다", "깃허브", "블로그", 지수.getNickname(), 지수.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 지수.getRegionTag().getName()));
//        로제.getProfile().changeProfile(new ProfileResponseDto("안녕하세요. 로제입니다!", "https://amachon.s3.ap-northeast-2.amazonaws.com/amachon/bd5fc393-fa7a-4f5c-b5a2-e84ae4cc6dee_%EB%A1%9C%EC%A0%9C.png", "개발을 좋아합니다", "깃허브", "블로그", 로제.getNickname(), 로제.getTechTags().stream().map(mt -> mt.getTechTag().getName()).collect(Collectors.toList()), 로제.getRegionTag().getName()));


        memberRepository.save(이정우);
        memberRepository.save(박종범);
        memberRepository.save(전재욱);
        memberRepository.save(전승현);
        memberRepository.save(허규범);
        memberRepository.save(카리나);
        memberRepository.save(윈터);
        memberRepository.save(닝닝);
        memberRepository.save(지젤);
        memberRepository.save(카즈하);
        memberRepository.save(김채원);
        memberRepository.save(허윤진);
        memberRepository.save(사쿠라);
        memberRepository.save(홍은채);
        memberRepository.save(장원영);
        memberRepository.save(레이);
        memberRepository.save(안유진);
        memberRepository.save(이서);
        memberRepository.save(가을);
        memberRepository.save(리즈);
        memberRepository.save(하니);
        memberRepository.save(민지);
        memberRepository.save(해린);
        memberRepository.save(다니엘);
        memberRepository.save(혜인);
        memberRepository.save(제니);
        memberRepository.save(리사);
        memberRepository.save(지수);
        memberRepository.save(로제);

        // 메시지
        sendMessageByMember(이정우, 카리나, new SendMessageDto("카리나님 안녕하세요!"));
        sendMessageByMember(카리나, 이정우, new SendMessageDto("이정우님 안녕하세요~"));
        sendMessageByMember(이정우, 카리나, new SendMessageDto("프로젝트 같이 하실래요?"));
        sendMessageByMember(카리나, 이정우, new SendMessageDto("어떤 스택 사용하나요?"));
        sendMessageByMember(이정우, 카리나, new SendMessageDto("스프링이랑 리액트 사용해서 웹 만드려구요!"));
        sendMessageByMember(허윤진, 이정우, new SendMessageDto("정우님 안녕하세요~ 혹시 프론트엔드 팀 구하셨나요?"));
        sendMessageByMember(이정우, 허윤진, new SendMessageDto("한자리 남았어요! 같이 하실래요?"));
        sendMessageByMember(허윤진, 이정우, new SendMessageDto("오 좋습니다! 참가 신청할게요!"));
        sendMessageByMember(박종범, 이정우, new SendMessageDto("야 정우야 프론트엔드 팀원좀 구해줘"));
        sendMessageByMember(이정우, 박종범, new SendMessageDto("지금 구하고있어ㅋㅋ 좀만 기달"));
        sendMessageByMember(박종범, 이정우, new SendMessageDto("살려줘.."));
        sendMessageByMember(이정우, 전재욱, new SendMessageDto("재욱아 디자인좀 부탁하자"));
        sendMessageByMember(전재욱, 이정우, new SendMessageDto("ㅋㅋㅇㅋ 뭐하면 되는데"));
        sendMessageByMember(박종범, 전승현, new SendMessageDto("승현아 깃 에러나는데 수정좀"));
        sendMessageByMember(전승현, 박종범, new SendMessageDto("ㅇㅋㅇㅋ"));
        sendMessageByMember(박종범, 허규범, new SendMessageDto("규범아 작업한거 깃에 푸시해주라"));
        sendMessageByMember(전재욱, 카즈하, new SendMessageDto("카즈하님 혹시 프로젝트 같이 하실래요? 프론트엔드 한자리가 남아서요."));
        sendMessageByMember(카즈하, 전재욱, new SendMessageDto("아 죄송합니다.. 이미 프로젝트 구해서요ㅠㅠ"));
        sendMessageByMember(전재욱, 카즈하, new SendMessageDto("아.. 넵!!!"));
        sendMessageByMember(전승현, 이서, new SendMessageDto("이서야 안녕"));
        sendMessageByMember(이서, 전승현, new SendMessageDto("누구세요..?"));
        sendMessageByMember(전승현, 이서, new SendMessageDto("아 잘못보냈네요. 죄송합니다."));
        sendMessageByMember(윈터, 박종범, new SendMessageDto("종범님 안녕하세요!"));
        sendMessageByMember(윈터, 박종범, new SendMessageDto("혹시 프론트엔드 자리있나요!?"));
        sendMessageByMember(박종범, 윈터, new SendMessageDto("리액트 한자리 남았습니다! 참가 신청 바로 하시면 될거같아요!"));
        sendMessageByMember(윈터, 박종범, new SendMessageDto("넹"));
        sendMessageByMember(레이, 허규범, new SendMessageDto("규범님 안녕하세요. 혹시 게임 개발 같이하실래요?"));
        sendMessageByMember(허규범, 레이, new SendMessageDto("안녕하세요! 무슨 게임 개발하시나요??"));
        sendMessageByMember(레이, 허규범, new SendMessageDto("FPS 게임 만들고 싶은데 규범님은 무슨 게임 좋아하세요?"));
        sendMessageByMember(허규범, 레이, new SendMessageDto("오 저도 FPS 게임 좋아하는데!! 간단한거라도 같이 해보실래요?"));
        sendMessageByMember(레이, 허규범, new SendMessageDto("좋아요~"));
        sendMessageByMember(장원영, 이정우, new SendMessageDto("안녕하세요 정우님~ 혹시 프론트엔드 플러터도 사용하실까요?"));
        sendMessageByMember(이정우, 장원영, new SendMessageDto("아.. 아직 플러터는 사용 예정이 없습니다ㅠㅠ"));
        sendMessageByMember(장원영, 이정우, new SendMessageDto("아 넵 알겠습니다ㅠㅠ"));
        sendMessageByMember(이정우, 장원영, new SendMessageDto("플러터 사용하게 된다면 연락드릴게요!"));
        sendMessageByMember(장원영, 이정우, new SendMessageDto("네~"));
        sendMessageByMember(박종범, 레이, new SendMessageDto("레이님 안녕하세요~ 디자이너 구하고 있는데 혹시 하실 생각 있으세요!?"));
        sendMessageByMember(레이, 박종범, new SendMessageDto("아 죄송해요.. 게임 개발 하기로해서ㅠㅠ 다음에 같이해요!"));
        sendMessageByMember(박종범, 레이, new SendMessageDto("앗 넵!!"));
        sendMessageByMember(박종범, 지수, new SendMessageDto("안녕하세요~ 저희가 디자이너가 구하고 있는데 혹시 참여 하실 생각 있으신가용?"));
        sendMessageByMember(지수, 박종범, new SendMessageDto("오 좋아요!! 프로젝트 아이디어가 좋은거같아요 ㅎㅎ"));
        sendMessageByMember(박종범, 지수, new SendMessageDto("감사합니다ㅎㅎ 참가 신청 해주시면 바로 수락해드릴게요!"));
        sendMessageByMember(지수, 박종범, new SendMessageDto("앗 알겠습니다~"));
        sendMessageByMember(전승현, 리즈, new SendMessageDto("리즈님 안녕하세요. 혹시 저희 프로젝트 참여 하실 생각있으신가요?"));
        sendMessageByMember(리즈, 전승현, new SendMessageDto("혹시 장고로 프로젝트 하는거 맞나요?"));
        sendMessageByMember(전승현, 리즈, new SendMessageDto("넵 맞습니다! 리즈님이 디자인 해주실 수 있을까요?"));
        sendMessageByMember(리즈, 전승현, new SendMessageDto("디자인을 최근에 배워서.. 그래도 괜찮을까요?!"));
        sendMessageByMember(전승현, 리즈, new SendMessageDto("괜찮습니다ㅎㅎ 같이 배우면서 해요~"));
        sendMessageByMember(리즈, 전승현, new SendMessageDto("좋아요~~~"));
        sendMessageByMember(해린, 전재욱, new SendMessageDto("재욱님 안녕하세요!! 백엔드 한 명이 부족해서 혹시 같이 프로젝트 하실래용?"));
        sendMessageByMember(전재욱, 해린, new SendMessageDto("오 좋습니다! 프론트엔드는 어떤 프레임워크 사용하나요?"));
        sendMessageByMember(해린, 전재욱, new SendMessageDto("Swift나 Android 사용할 것 같아요!"));
        sendMessageByMember(전재욱, 해린, new SendMessageDto("네 알겠습니다~ 참가 신청 할게요!"));

        // 프로젝트
        Project project1 = Project.builder().title("이정우의 스프링 프로젝트").description("스프링 프로젝트")
                .leader(이정우).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(7L)).build();
        projectRepository.save(project1);
        projectTechTagRepository.save(new ProjectTechTag(project1, flutter));
        projectTechTagRepository.save(new ProjectTechTag(project1, spring));
        projectTechTagRepository.save(new ProjectTechTag(project1, android));

        Project project2 = Project.builder().title("박종범의 리액트 프로젝트").description("리액트 프로젝트")
                .leader(박종범).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(14L)).build();
        projectRepository.save(project2);
        projectTechTagRepository.save(new ProjectTechTag(project2, react));
        projectTechTagRepository.save(new ProjectTechTag(project2, spring));
        projectTechTagRepository.save(new ProjectTechTag(project2, reactNative));

        Project project3 = Project.builder().title("전재욱의 안드로이드 프로젝트").description("안드로이드 프로젝트")
                .leader(전재욱).regionTag(의정부시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(27L)).build();
        projectRepository.save(project3);
        projectTechTagRepository.save(new ProjectTechTag(project3, android));
        projectTechTagRepository.save(new ProjectTechTag(project3, spring));
        projectTechTagRepository.save(new ProjectTechTag(project3, aws));

        Project project4 = Project.builder().title("전승현의 장고 프로젝트").description("장고 프로젝트")
                .leader(전승현).regionTag(성남시_분당구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(15L)).build();
        projectRepository.save(project4);
        projectTechTagRepository.save(new ProjectTechTag(project4, django));
        projectTechTagRepository.save(new ProjectTechTag(project4, flutter));
        projectTechTagRepository.save(new ProjectTechTag(project4, mongoDB));

        Project project5 = Project.builder().title("허규범의 유니티 프로젝트").description("유니티 프로젝트")
                .leader(허규범).regionTag(수원시_영통구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(1L)).build();
        projectRepository.save(project5);
        projectTechTagRepository.save(new ProjectTechTag(project5, unity));
        projectTechTagRepository.save(new ProjectTechTag(project5, reactNative));
        projectTechTagRepository.save(new ProjectTechTag(project5, kubernetes));

        Project project6 = Project.builder().title("이정우의 안드로이드 프로젝트").description("안드로이드 프로젝트")
                .leader(이정우).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(4L)).build();
        projectRepository.save(project6);
        projectTechTagRepository.save(new ProjectTechTag(project6, android));
        projectTechTagRepository.save(new ProjectTechTag(project6, nestjs));
        projectTechTagRepository.save(new ProjectTechTag(project6, aws));

        Project project7 = Project.builder().title("박종범의 안드로이드 프로젝트").description("안드로이드 프로젝트")
                .leader(박종범).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(22L)).build();
        projectRepository.save(project7);
        projectTechTagRepository.save(new ProjectTechTag(project7, nodejs));
        projectTechTagRepository.save(new ProjectTechTag(project7, android));
        projectTechTagRepository.save(new ProjectTechTag(project7, docker));

        Project project8 = Project.builder().title("전재욱의 플러터 프로젝트").description("플러터 프로젝트")
                .leader(전재욱).regionTag(의정부시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(10L)).build();
        projectRepository.save(project8);
        projectTechTagRepository.save(new ProjectTechTag(project8, flutter));
        projectTechTagRepository.save(new ProjectTechTag(project8, spring));
        projectTechTagRepository.save(new ProjectTechTag(project8, mySQL));

        Project project9 = Project.builder().title("전승현의 리액트 프로젝트").description("리액트 프로젝트")
                .leader(전승현).regionTag(성남시_분당구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(11L)).build();
        projectRepository.save(project9);
        projectTechTagRepository.save(new ProjectTechTag(project9, react));
        projectTechTagRepository.save(new ProjectTechTag(project9, express));
        projectTechTagRepository.save(new ProjectTechTag(project9, figma));

        Project project10 = Project.builder().title("허규범의 안드로이드 프로젝트").description("안드로이드 프로젝트")
                .leader(허규범).regionTag(수원시_영통구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(19L)).build();
        projectRepository.save(project10);
        projectTechTagRepository.save(new ProjectTechTag(project10, android));
        projectTechTagRepository.save(new ProjectTechTag(project10, spring));
        projectTechTagRepository.save(new ProjectTechTag(project10, figma));

        Project project11 = Project.builder().title("공모전 나갈사람 구합니다").description("공모전")
                .leader(카리나).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(17L)).build();
        projectRepository.save(project11);
        projectTechTagRepository.save(new ProjectTechTag(project11, figma));
        projectTechTagRepository.save(new ProjectTechTag(project11, nodejs));
        projectTechTagRepository.save(new ProjectTechTag(project11, go));

        Project project12 = Project.builder().title("해커톤 나갈사람 구해요").description("해커톤")
                .leader(윈터).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(17L)).build();
        projectRepository.save(project12);
        projectTechTagRepository.save(new ProjectTechTag(project12, flutter));
        projectTechTagRepository.save(new ProjectTechTag(project12, nodejs));
        projectTechTagRepository.save(new ProjectTechTag(project12, mongoDB));

        Project project13 = Project.builder().title("쇼핑몰 같이 만드실분").description("쇼핑몰")
                .leader(닝닝).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(13L)).build();
        projectRepository.save(project13);
        projectTechTagRepository.save(new ProjectTechTag(project13, react));
        projectTechTagRepository.save(new ProjectTechTag(project13, spring));
        projectTechTagRepository.save(new ProjectTechTag(project13, aws));

        Project project14 = Project.builder().title("당근마켓 클론 코딩").description("당근마켓")
                .leader(지젤).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(6L)).build();
        projectRepository.save(project14);
        projectTechTagRepository.save(new ProjectTechTag(project14, react));
        projectTechTagRepository.save(new ProjectTechTag(project14, spring));
        projectTechTagRepository.save(new ProjectTechTag(project14, reactNative));

        Project project15 = Project.builder().title("게임 만들사람 손").description("게임")
                .leader(카즈하).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(30L)).build();
        projectRepository.save(project15);
        projectTechTagRepository.save(new ProjectTechTag(project15, unity));
        projectTechTagRepository.save(new ProjectTechTag(project15, spring));
        projectTechTagRepository.save(new ProjectTechTag(project15, docker));

        Project project16 = Project.builder().title("포트폴리오 만들사람 구해요").description("포트폴리오")
                .leader(김채원).regionTag(강동구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(37L)).build();
        projectRepository.save(project16);
        projectTechTagRepository.save(new ProjectTechTag(project16, flutter));
        projectTechTagRepository.save(new ProjectTechTag(project16, spring));
        projectTechTagRepository.save(new ProjectTechTag(project16, figma));

        Project project17 = Project.builder().title("백엔드 포트폴리오 같이할사람").description("백엔드")
                .leader(허윤진).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(8L)).build();
        projectRepository.save(project17);
        projectTechTagRepository.save(new ProjectTechTag(project17, swift));
        projectTechTagRepository.save(new ProjectTechTag(project17, spring));
        projectTechTagRepository.save(new ProjectTechTag(project17, aws));

        Project project18 = Project.builder().title("프론트엔드 포트폴리오 만들사람").description("프론트엔드")
                .leader(사쿠라).regionTag(서대문구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(9L)).build();
        projectRepository.save(project18);
        projectTechTagRepository.save(new ProjectTechTag(project18, react));
        projectTechTagRepository.save(new ProjectTechTag(project18, firebase));
        projectTechTagRepository.save(new ProjectTechTag(project18, figma));

        Project project19 = Project.builder().title("심심한 사람끼리").description("심심한")
                .leader(홍은채).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(11L)).build();
        projectRepository.save(project19);
        projectTechTagRepository.save(new ProjectTechTag(project19, svelte));
        projectTechTagRepository.save(new ProjectTechTag(project19, go));
        projectTechTagRepository.save(new ProjectTechTag(project19, mongoDB));

        Project project20 = Project.builder().title("사이드 프로젝트 백엔드 모집해요").description("백엔드 모집")
                .leader(장원영).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(15L)).build();
        projectRepository.save(project20);
        projectTechTagRepository.save(new ProjectTechTag(project20, react));
        projectTechTagRepository.save(new ProjectTechTag(project20, spring));
        projectTechTagRepository.save(new ProjectTechTag(project20, mySQL));

        Project project21 = Project.builder().title("사이드 프로젝트 프론트엔트 모집해요").description("프론트엔드 모집")
                .leader(레이).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(3L)).build();
        projectRepository.save(project21);
        projectTechTagRepository.save(new ProjectTechTag(project21, react));
        projectTechTagRepository.save(new ProjectTechTag(project21, figma));
        projectTechTagRepository.save(new ProjectTechTag(project21, firebase));

        Project project22 = Project.builder().title("운동 플랫폼 만들사람 구해요").description("운동 플랫폼")
                .leader(안유진).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(7L)).build();
        projectRepository.save(project22);
        projectTechTagRepository.save(new ProjectTechTag(project22, android));
        projectTechTagRepository.save(new ProjectTechTag(project22, django));
        projectTechTagRepository.save(new ProjectTechTag(project22, mySQL));

        Project project23 = Project.builder().title("iOS 앱 개발 프로젝트에서 디자이너 모셔요").description("디자이너 모집")
                .leader(가을).regionTag(의정부시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(7L)).build();
        projectRepository.save(project23);
        projectTechTagRepository.save(new ProjectTechTag(project23, figma));
        projectTechTagRepository.save(new ProjectTechTag(project23, aws));
        projectTechTagRepository.save(new ProjectTechTag(project23, git));

        Project project24 = Project.builder().title("새싹톤 나가실 프론트엔드분 구합니다").description("프론트엔드 모집")
                .leader(리즈).regionTag(용인시_처인구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(3L)).build();
        projectRepository.save(project24);
        projectTechTagRepository.save(new ProjectTechTag(project24, react));
        projectTechTagRepository.save(new ProjectTechTag(project24, spring));
        projectTechTagRepository.save(new ProjectTechTag(project24, mongoDB));

        Project project25 = Project.builder().title("랜덤 음악 추천 플랫폼 프로젝트").description("디자이너, 프론트엔드 모집")
                .leader(하니).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(21L)).build();
        projectRepository.save(project25);
        projectTechTagRepository.save(new ProjectTechTag(project25, react));
        projectTechTagRepository.save(new ProjectTechTag(project25, figma));
        projectTechTagRepository.save(new ProjectTechTag(project25, aws));

        Project project26 = Project.builder().title("교육 서비스 프로젝트 백엔드 모집합니다").description("백엔드 모집")
                .leader(민지).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(70L)).build();
        projectRepository.save(project26);
        projectTechTagRepository.save(new ProjectTechTag(project26, nodejs));
        projectTechTagRepository.save(new ProjectTechTag(project26, spring));
        projectTechTagRepository.save(new ProjectTechTag(project26, aws));

        Project project27 = Project.builder().title("새싹톤 팀 꾸려요").description("백엔드, 디자이너 모집")
                .leader(해린).regionTag(화성시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(31L)).build();
        projectRepository.save(project27);
        projectTechTagRepository.save(new ProjectTechTag(project27, figma));
        projectTechTagRepository.save(new ProjectTechTag(project27, spring));
        projectTechTagRepository.save(new ProjectTechTag(project27, kubernetes));

        Project project28 = Project.builder().title("패션정보 공유 커뮤니티에서 프론트엔드 분 모셔요").description("프론트엔드 모집")
                .leader(다니엘).regionTag(의정부시).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(18L)).build();
        projectRepository.save(project28);
        projectTechTagRepository.save(new ProjectTechTag(project28, react));
        projectTechTagRepository.save(new ProjectTechTag(project28, reactNative));
        projectTechTagRepository.save(new ProjectTechTag(project28, flutter));

        Project project29 = Project.builder().title("토이 앱 개발 프로젝트 합류하실 디자이너 구합니다").description("디자이너 모집")
                .leader(혜인).regionTag(수원시_영통구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(13L)).build();
        projectRepository.save(project29);
        projectTechTagRepository.save(new ProjectTechTag(project29, figma));
        projectTechTagRepository.save(new ProjectTechTag(project29, aws));
        projectTechTagRepository.save(new ProjectTechTag(project29, docker));

        Project project30 = Project.builder().title("서비스 개발 크루").description("크루 모집")
                .leader(제니).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(2L)).build();
        projectRepository.save(project30);
        projectTechTagRepository.save(new ProjectTechTag(project30, go));
        projectTechTagRepository.save(new ProjectTechTag(project30, express));
        projectTechTagRepository.save(new ProjectTechTag(project30, javaScript));

        Project project31 = Project.builder().title("신입 취업용 프로젝트 백엔드 구해요").description("백엔드 모집")
                .leader(리사).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(4L)).build();
        projectRepository.save(project31);
        projectTechTagRepository.save(new ProjectTechTag(project31, aws));
        projectTechTagRepository.save(new ProjectTechTag(project31, spring));
        projectTechTagRepository.save(new ProjectTechTag(project31, mySQL));

        Project project32 = Project.builder().title("새싹톤 나가실 프론트엔드 구합니다").description("프론트엔드 모집")
                .leader(지수).regionTag(수원시_영통구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(7L)).build();
        projectRepository.save(project32);
        projectTechTagRepository.save(new ProjectTechTag(project32, swift));
        projectTechTagRepository.save(new ProjectTechTag(project32, figma));
        projectTechTagRepository.save(new ProjectTechTag(project32, firebase));

        Project project33 = Project.builder().title("새싹톤 나가실 백엔드 구해요").description("백엔드 모집")
                .leader(로제).regionTag(강남구).recruitDeadline(LocalDate.now()).recruitNumber(4).developPeriod(LocalDate.now().plusDays(3L)).build();
        projectRepository.save(project33);
        projectTechTagRepository.save(new ProjectTechTag(project33, aws));
        projectTechTagRepository.save(new ProjectTechTag(project33, nestjs));
        projectTechTagRepository.save(new ProjectTechTag(project33, mySQL));

        teamMemberRepository.save(new TeamMember(project1, 카리나));
        teamMemberRepository.save(new TeamMember(project1, 허윤진));
        teamMemberRepository.save(new TeamMember(project1, 장원영));
        teamMemberRepository.save(new TeamMember(project2, 민지));
        teamMemberRepository.save(new TeamMember(project2, 해린));
        teamMemberRepository.save(new TeamMember(project2, 레이));
        teamMemberRepository.save(new TeamMember(project3, 지수));
        teamMemberRepository.save(new TeamMember(project3, 장원영));
        teamMemberRepository.save(new TeamMember(project3, 카리나));
        teamMemberRepository.save(new TeamMember(project4, 허윤진));
        teamMemberRepository.save(new TeamMember(project4, 레이));
        teamMemberRepository.save(new TeamMember(project5, 이서));
        teamMemberRepository.save(new TeamMember(project5, 윈터));
        teamMemberRepository.save(new TeamMember(project5, 지젤));
        teamMemberRepository.save(new TeamMember(project6, 닝닝));
        teamMemberRepository.save(new TeamMember(project6, 가을));
        teamMemberRepository.save(new TeamMember(project7, 박종범));
        teamMemberRepository.save(new TeamMember(project8, 허규범));
        teamMemberRepository.save(new TeamMember(project8, 전재욱));
        teamMemberRepository.save(new TeamMember(project8, 레이));
        teamMemberRepository.save(new TeamMember(project9, 이정우));
        teamMemberRepository.save(new TeamMember(project10, 이서));
        teamMemberRepository.save(new TeamMember(project10, 전승현));
        teamMemberRepository.save(new TeamMember(project11, 로제));
        teamMemberRepository.save(new TeamMember(project11, 지수));
        teamMemberRepository.save(new TeamMember(project12, 리사));
        teamMemberRepository.save(new TeamMember(project13, 카리나));
        teamMemberRepository.save(new TeamMember(project13, 윈터));
        teamMemberRepository.save(new TeamMember(project14, 다니엘));
        teamMemberRepository.save(new TeamMember(project15, 해린));
        teamMemberRepository.save(new TeamMember(project15, 혜인));
        teamMemberRepository.save(new TeamMember(project16, 닝닝));
        teamMemberRepository.save(new TeamMember(project17, 지젤));
        teamMemberRepository.save(new TeamMember(project17, 카리나));
        teamMemberRepository.save(new TeamMember(project18, 카즈하));
        teamMemberRepository.save(new TeamMember(project18, 허윤진));
        teamMemberRepository.save(new TeamMember(project19, 김채원));
        teamMemberRepository.save(new TeamMember(project20, 사쿠라));
        teamMemberRepository.save(new TeamMember(project20, 윈터));
        teamMemberRepository.save(new TeamMember(project21, 박종범));
        teamMemberRepository.save(new TeamMember(project21, 허윤진));
        teamMemberRepository.save(new TeamMember(project22, 장원영));
        teamMemberRepository.save(new TeamMember(project22, 홍은채));
        teamMemberRepository.save(new TeamMember(project23, 민지));
        teamMemberRepository.save(new TeamMember(project24, 카리나));
        teamMemberRepository.save(new TeamMember(project24, 윈터));
        teamMemberRepository.save(new TeamMember(project25, 허규범));
        teamMemberRepository.save(new TeamMember(project26, 전재욱));
        teamMemberRepository.save(new TeamMember(project26, 전승현));
        teamMemberRepository.save(new TeamMember(project27, 안유진));
        teamMemberRepository.save(new TeamMember(project28, 이서));
        teamMemberRepository.save(new TeamMember(project29, 리즈));
        teamMemberRepository.save(new TeamMember(project30, 해린));
        teamMemberRepository.save(new TeamMember(project31, 카즈하));

    }

    public void sendMessageByMember(Member from, Member to, SendMessageDto sendMessageDto) {
        Optional<MessageRoom> messageRoom = messageRoomRepository.findByFromAndTo(from.getId(), to.getId());
        if (messageRoom.isEmpty()) {
            MessageRoom fromMessageRoom = MessageRoom.builder().from(from).to(to).build();
            MessageRoom toMessageRoom = MessageRoom.builder().from(to).to(from).build();
            fromMessageRoom.match(toMessageRoom);
            messageRoomRepository.save(fromMessageRoom);
            messageRoomRepository.save(toMessageRoom);
            messageRoom = Optional.of(fromMessageRoom);
        } else if (messageRoom.get().getStatus() == BaseEntity.Status.EXPIRED) {
            messageRoom.get().init();
        }
        messageService.send(messageRoom.get(), sendMessageDto);
    }
}