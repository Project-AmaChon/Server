package v1.amachon.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import v1.amachon.common.RepositoryTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.fixtures.MemberFixtures;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(MemberFixtures.정우());
        memberRepository.save(MemberFixtures.종범());
        memberRepository.save(MemberFixtures.규범());
        memberRepository.save(MemberFixtures.승현());
        memberRepository.save(MemberFixtures.재욱());
    }

    @Test
    @DisplayName("이메일을 통해 사용자를 조회할 수 있다.")
    public void findMember() {
        // when
        Member member = memberRepository.findByEmail(MemberFixtures.정우_이메일).get();

        // then
        assertAll(
                () -> assertThat(member.getId()).isNotNull(),
                () -> assertThat(member.getEmail()).isEqualTo(MemberFixtures.정우_이메일),
                () -> assertThat(member.getNickname()).isEqualTo(MemberFixtures.정우_닉네임),
                () -> assertThat(member.getPassword()).isEqualTo(MemberFixtures.정우_비밀번호)
        );
    }
}
