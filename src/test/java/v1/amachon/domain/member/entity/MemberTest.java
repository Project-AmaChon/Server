package v1.amachon.domain.member.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import v1.amachon.domain.member.repository.MemberRepository;

@SpringBootTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void createMemberTest() {
        Member member = Member.builder().email("이정우").nickname("이정우").password("이정우").build();
        memberRepository.save(member);
    }
}
