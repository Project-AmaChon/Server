package v1.amachon.fixtures;

import v1.amachon.member.entity.Member;
import v1.amachon.project.entity.Project;

import java.time.LocalDate;

public class ProjectFixtures {

    public static final String 정우_프로젝트_제목 = "정우의 프로젝트";
    public static final String 정우_프로젝트_설명 = "스프링 & 안드로이드 프로젝트";
    public static final LocalDate 정우_프로젝트_마감_기한 = LocalDate.of(2023, 4, 1);
    public static final LocalDate 정우_프로젝트_진행_기간 = LocalDate.of(2023, 7, 1);
    public static final int 정우_프로젝트_모집_인원 = 5;

    public static final String 종범_프로젝트_제목 = "종범의 프로젝트";
    public static final String 종범_프로젝트_설명 = "스프링 & 리액트 프로젝트";
    public static final LocalDate 종범_프로젝트_마감_기한 = LocalDate.of(2023, 7, 1);
    public static final LocalDate 종범_프로젝트_진행_기간 = LocalDate.of(2023, 9, 1);
    public static final int 종범_프로젝트_모집_인원 = 4;

    public static final String 승현_프로젝트_제목 = "승현의 프로젝트";
    public static final String 승현_프로젝트_설명 = "리액트 & 노드 프로젝트";
    public static final LocalDate 승현_프로젝트_마감_기한 = LocalDate.of(2023, 1, 1);
    public static final LocalDate 승현_프로젝트_진행_기간 = LocalDate.of(2023, 3, 1);
    public static final int 승현_프로젝트_모집_인원 = 6;

    public static final String 재욱_프로젝트_제목 = "재욱의 프로젝트";
    public static final String 재욱_프로젝트_설명 = "플러터 & 스프링 프로젝트";
    public static final LocalDate 재욱_프로젝트_마감_기한 = LocalDate.of(2023, 11, 1);
    public static final LocalDate 재욱_프로젝트_진행_기간 = LocalDate.of(2023, 12, 1);
    public static final int 재욱_프로젝트_모집_인원 = 5;

    public static final String 규범_프로젝트_제목 = "규범의 프로젝트";
    public static final String 규범_프로젝트_설명 = "노드 & ios 프로젝트";
    public static final LocalDate 규범_프로젝트_마감_기한 = LocalDate.of(2023, 4, 1);
    public static final LocalDate 규범_프로젝트_진행_기간 = LocalDate.of(2023, 12, 1);
    public static final int 규범_프로젝트_모집_인원 = 5;

    public static Project 정우_프로젝트(Member leader) {
        return Project.builder().leader(leader).title(정우_프로젝트_제목).description(정우_프로젝트_설명)
                        .recruitDeadline(정우_프로젝트_마감_기한).developPeriod(정우_프로젝트_진행_기간)
                        .recruitNumber(정우_프로젝트_모집_인원).build();
    }

    public static Project 재욱_프로젝트(Member leader) {
        return Project.builder().leader(leader).title(재욱_프로젝트_제목).description(재욱_프로젝트_설명)
                .recruitDeadline(재욱_프로젝트_마감_기한).developPeriod(재욱_프로젝트_진행_기간)
                .recruitNumber(재욱_프로젝트_모집_인원).build();
    }

    public static Project 승현_프로젝트(Member leader) {
        return Project.builder().leader(leader).title(승현_프로젝트_제목).description(승현_프로젝트_설명)
                .recruitDeadline(승현_프로젝트_마감_기한).developPeriod(승현_프로젝트_진행_기간)
                .recruitNumber(승현_프로젝트_모집_인원).build();
    }

    public static Project 종범_프로젝트(Member leader) {
        return Project.builder().leader(leader).title(종범_프로젝트_제목).description(종범_프로젝트_설명)
                .recruitDeadline(종범_프로젝트_마감_기한).developPeriod(종범_프로젝트_진행_기간)
                .recruitNumber(종범_프로젝트_모집_인원).build();
    }

    public static Project 규범_프로젝트(Member leader) {
        return Project.builder().leader(leader).title(규범_프로젝트_제목).description(규범_프로젝트_설명)
                .recruitDeadline(규범_프로젝트_마감_기한).developPeriod(규범_프로젝트_진행_기간)
                .recruitNumber(규범_프로젝트_모집_인원).build();
    }

}
