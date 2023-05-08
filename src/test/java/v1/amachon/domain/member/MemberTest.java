package v1.amachon.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;

@SpringBootTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 생성")
    public void createMemberTest() {
        Member member = Member.builder().email("이정우").nickname("이정우").password("이정우").build();
        memberRepository.save(member);
    }
}
