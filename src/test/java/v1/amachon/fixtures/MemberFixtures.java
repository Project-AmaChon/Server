package v1.amachon.fixtures;

import v1.amachon.common.RepositoryTest;
import v1.amachon.member.entity.Member;

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

    public static Member 정우() {
        return new Member(정우_이메일, 정우_닉네임, 정우_비밀번호);
    }

    public static Member 승현() {
        return new Member(승현_이메일, 승현_닉네임, 승현_비밀번호);
    }

    public static Member 재욱() {
        return new Member(재욱_이메일, 재욱_닉네임, 재욱_비밀번호);
    }

    public static Member 종범() {
        return new Member(종범_이메일, 종범_닉네임, 종범_비밀번호);
    }

    public static Member 규범() {
        return new Member(규범_이메일, 규범_닉네임, 규범_비밀번호);
    }
}
