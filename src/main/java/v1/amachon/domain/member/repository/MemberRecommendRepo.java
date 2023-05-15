package v1.amachon.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import v1.amachon.domain.member.dto.RecommendCond;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.project.dto.recruit.RecruitManagementDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static v1.amachon.domain.member.entity.QMember.member;
import static v1.amachon.domain.tags.entity.techtag.QMemberTechTag.memberTechTag;
import static v1.amachon.domain.tags.entity.techtag.QTechTag.techTag;

@Repository
public class MemberRecommendRepo {

    private final JPAQueryFactory queryFactory;

    public MemberRecommendRepo(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private BooleanExpression regionTagIn(List<String> regionTagNames) {
        return techTagIn(Collections.emptyList()).and(regionTagIn(regionTagNames));
    }

    public BooleanExpression techTagIn(List<String> techTagNames) {
        return techTagIn(techTagNames).and(regionTagIn(Collections.emptyList()));
    }

    private NumberTemplate<Long> techTagAndRegionTagIn(RecommendCond cond) {
        return Expressions.numberTemplate(Long.class,
                "CASE WHEN {0} AND {1} THEN 2 WHEN {0} OR {1} THEN 1 ELSE 0 END",
                techTagIn(cond.getTechTagNames()), regionTagIn(cond.getRegionTagNames()));
    }

    public List<RecruitManagementDto> getRecommendMemberByCond(RecommendCond cond) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> members = queryFactory.select(member)
                .from(member)
                .innerJoin(member.techTags, memberTechTag)
                .innerJoin(memberTechTag.techTag, techTag)
                .where(techTagAndRegionTagIn(cond).gt(0),
                        techTagIn(cond.getTechTagNames()),
                        regionTagIn(cond.getRegionTagNames()))
                .groupBy(member.id)
                .orderBy(techTagAndRegionTagIn(cond).desc(),
                        techTagIn(cond.getTechTagNames()).desc(),
                        regionTagIn(cond.getRegionTagNames()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return members.stream().map(RecruitManagementDto::new).collect(Collectors.toList());
    }
}
