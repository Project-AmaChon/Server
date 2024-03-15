package v1.amachon.project.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.project.service.response.ProjectResponse;
import v1.amachon.project.service.request.ProjectSearchCond;
import v1.amachon.project.entity.Project;

import java.util.List;
import java.util.stream.Collectors;

import static v1.amachon.project.entity.QProject.project;
import static v1.amachon.tags.entity.techtag.QProjectTechTag.projectTechTag;
import static v1.amachon.tags.entity.techtag.QTechTag.techTag;


@Repository
public class ProjectSearchRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectSearchRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private BooleanExpression keywordLike(String keyword) {
        return (keyword == null || keyword.length() == 0) ? null : project.title.like("%" + keyword + "%");
    }

    private BooleanExpression regionTagIn(List<String> regionTagNames) {
        return regionTagNames.isEmpty() ? null : project.regionTag.name.in(regionTagNames);
    }

    private BooleanExpression techTagIn(List<String> techTagNames) {
        return techTagNames.isEmpty() ? null : techTag.name.in(techTagNames);
    }

    private BooleanExpression techTagCountEqCond(List<String> techTagNames) {
        return techTagNames.isEmpty() ? null : projectTechTag.count().eq((long) techTagNames.size());
    }

    public List<ProjectResponse> searchProjectByAllCond(ProjectSearchCond cond) {
        List<Project> projects = queryFactory.selectDistinct(project)
                .from(project)
                .join(project.techTags, projectTechTag)
                .join(projectTechTag.techTag, techTag)
                .where(project.in(JPAExpressions
                        .selectDistinct(project)
                        .from(project)
                        .join(project.techTags, projectTechTag)
                        .join(projectTechTag.techTag, techTag)
                        .where(keywordLike(cond.getKeyword()),
                                techTagIn(cond.getTechTagNames()),
                                regionTagIn(cond.getRegionTagNames()),
                                project.status.eq(BaseEntity.Status.NORMAL))
                        .groupBy(project.id)
                        .having(techTagCountEqCond(cond.getTechTagNames()))
                        )
                )
                .fetch();
        return projects.stream().map(ProjectResponse::new).collect(Collectors.toList());
    }
}
