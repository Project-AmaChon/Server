package v1.amachon.domain.member.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import v1.amachon.domain.member.service.dto.RecommendCond;
import v1.amachon.domain.member.entity.Member;
import v1.amachon.domain.project.service.dto.recruit.RecruitManagementDto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static v1.amachon.domain.member.entity.QMember.member;
import static v1.amachon.domain.member.entity.QProfile.profile;
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
        return techTagNames.isEmpty() ? null : memberTechTag.techTag.name.in(techTagNames);
    }

    public List<RecruitManagementDto> getRecommendMemberByCond(RecommendCond cond) {
        NumberExpression<Long> tagCountExpression = Expressions.asNumber(memberTechTag.id.count().add(
                Expressions.cases()
                        .when(member.regionTag.name.in(cond.getRegionTagNames())).then(5)
                        .otherwise(0)));

        Pageable pageable = PageRequest.of(0, 10);
        List<Tuple> result = queryFactory.select(member, Expressions.asNumber(tagCountExpression.as("tag_count")))
                .from(member)
                .innerJoin(member.profile, profile).fetchJoin()
                .innerJoin(member.techTags, memberTechTag)
                .innerJoin(memberTechTag.techTag, techTag)
                .where(techTagIn(cond.getTechTagNames()))
                .groupBy(member.id)
                .orderBy(tagCountExpression.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Member> converted = result.stream().map(r -> r.get(member)).collect(Collectors.toList());
        Set<Member> members = new LinkedHashSet<>(converted);
        return members.stream().map(RecruitManagementDto::new).collect(Collectors.toList());
    }
}
