package v1.amachon.domain.member.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        return regionTagNames.isEmpty() ? null : member.regionTag.name.in(regionTagNames);
    }

    public BooleanExpression techTagIn(List<String> techTagNames) {
        return techTagNames.isEmpty() ? null : techTag.name.in(techTagNames);
    }

    private OrderSpecifier<Integer> techTagAndRegionTagCond(RecommendCond cond) {
        return Expressions.asNumber(
                        Expressions.cases()
                                .when(techTagIn(cond.getTechTagNames()).and(regionTagIn(cond.getRegionTagNames()))).then(2)
                                .when(techTagIn(cond.getTechTagNames())).then(1)
                                .otherwise(0))
                                .desc();
    }

    public List<RecruitManagementDto> getRecommendMemberByCond(RecommendCond cond) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> result = queryFactory.select(member)
                .from(member)
                .innerJoin(member.techTags, memberTechTag)
                .innerJoin(memberTechTag.techTag, techTag)
                .where(techTagIn(cond.getTechTagNames()))
                .orderBy(techTagAndRegionTagCond(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Set<Member> members = new LinkedHashSet<>(result);
        return members.stream().map(RecruitManagementDto::new).collect(Collectors.toList());
    }
}
