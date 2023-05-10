package v1.amachon.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v1.amachon.domain.base.BaseException;
import v1.amachon.domain.base.BaseResponseStatus;
import v1.amachon.domain.member.dto.join.JoinDto;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.member.repository.MemberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(JoinDto joinDto) {
        memberRepository.save(Member.ofMember(joinDto));
    }

    public void isDuplicateEmail(String email) throws BaseException {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isEmpty()) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
        }
    }
}
