package v1.amachon.project.repository;

import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import v1.amachon.member.service.dto.RecommendCond;
import v1.amachon.member.entity.Member;
import v1.amachon.project.service.response.RecommendMemberResponse;

import java.util.List;
import java.util.stream.Collectors;

import static v1.amachon.member.entity.QMember.member;
import static v1.amachon.tags.entity.regiontag.QRegionTag.regionTag;
import static v1.amachon.tags.entity.techtag.QMemberTechTag.memberTechTag;
import static v1.amachon.tags.entity.techtag.QTechTag.techTag;

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
        return techTagNames.isEmpty() ? null : memberTechTag.techTag.name.in(techTagNames);
    }

    public List<RecommendMemberResponse> getRecommendMemberByCond(RecommendCond cond) {
        NumberExpression<Long> regionTagCountExpression = Expressions.asNumber(
                Expressions.cases()
                        .when(member.regionTag.name.eq(cond.getRegionTagName())).then(5l)
                        .otherwise(0l));

        NumberExpression<Long> techTagCountExpression = Expressions.asNumber(memberTechTag.id.count().multiply(4));

        Pageable pageable = PageRequest.of(0, 10);
        List<Member> members = queryFactory.select(member)
                .from(member)
                .innerJoin(member.techTags, memberTechTag)
                .innerJoin(memberTechTag.techTag, techTag)
                .innerJoin(member.regionTag, regionTag).fetchJoin()
                .where(techTagIn(cond.getTechTagNames()))
                .groupBy(member.id)
                .orderBy(Expressions.asNumber(regionTagCountExpression.add(techTagCountExpression)).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return members.stream().map(RecommendMemberResponse::new).collect(Collectors.toList());
    }
}
