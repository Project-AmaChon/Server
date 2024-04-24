package v1.amachon.fixtures;

import v1.amachon.common.RepositoryTest;
import v1.amachon.member.entity.Member;
import v1.amachon.member.entity.vo.Profile;
import v1.amachon.member.service.dto.UpdateProfileRequestDto;

@RepositoryTest
public class MemberFixtures {
    // 정우
    public static final String 정우_이메일 = "acg1111@naver.com";
    public static final String 정우_닉네임 = "정우";
    public static final String 정우_비밀번호 = "1111";

    // 종범
    public static final String 종범_이메일 = "acg2222@naver.com";
    public static final String 종범_닉네임 = "종범";
    public static final String 종범_비밀번호 = "2222";

    // 승현
    public static final String 승현_이메일 = "acg3333@naver.com";
    public static final String 승현_닉네임 = "승현";
    public static final String 승현_비밀번호 = "3333";

    // 재욱
    public static final String 재욱_이메일 = "acg4444@naver.com";
    public static final String 재욱_닉네임 = "재욱";
    public static final String 재욱_비밀번호 = "4444";

    // 규범
    public static final String 규범_이메일 = "acg5555@naver.com";
    public static final String 규범_닉네임 = "규범";
    public static final String 규범_비밀번호 = "5555";

    public static final UpdateProfileRequestDto 공통_프로필 = new UpdateProfileRequestDto("자기 소개", "설명", "깃허브 url", "블로그 url");

    public static Member 정우() {
        Member 정우 = Member.builder().nickname(정우_닉네임).email(정우_이메일).password(정우_비밀번호).build();
        정우.changeProfile(공통_프로필);
        return 정우;
    }

    public static Member 승현() {
        Member 승현 = Member.builder().nickname(승현_닉네임).email(승현_이메일).password(승현_비밀번호).build();
        승현.changeProfile(공통_프로필);
        return 승현;
    }

    public static Member 재욱() {
        Member 재욱 = Member.builder().nickname(재욱_닉네임).email(재욱_이메일).password(재욱_비밀번호).build();
        재욱.changeProfile(공통_프로필);
        return 재욱;
    }

    public static Member 종범() {
        Member 종범 = Member.builder().nickname(종범_닉네임).email(종범_이메일).password(종범_비밀번호).build();
        종범.changeProfile(공통_프로필);
        return 종범;
    }

    public static Member 규범() {
        Member 규범 = Member.builder().nickname(규범_닉네임).email(규범_이메일).password(규범_비밀번호).build();
        규범.changeProfile(공통_프로필);
        return 규범;
    }
}
