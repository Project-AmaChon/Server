package v1.amachon.domain.project.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import v1.amachon.domain.project.dto.ProjectDto;
import v1.amachon.domain.project.dto.ProjectSearchCond;
import v1.amachon.domain.project.entity.Project;

import java.util.List;
import java.util.stream.Collectors;

import static v1.amachon.domain.project.entity.QProject.project;
import static v1.amachon.domain.tags.entity.techtag.QProjectTechTag.projectTechTag;
import static v1.amachon.domain.tags.entity.techtag.QTechTag.techTag;

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

    public List<ProjectDto> searchProjectByAllCond(ProjectSearchCond cond) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Tuple> result = queryFactory.select(project, Expressions.asNumber(projectTechTag.id.count()).as("tag_count"))
                .from(project)
                .innerJoin(project.techTags, projectTechTag).on()
                .innerJoin(projectTechTag.techTag, techTag)
                .where(keywordLike(cond.getKeyword()),
                        techTagIn(cond.getTechTagNames()),
                        regionTagIn(cond.getRegionTagNames()))
                .groupBy(project.id)
                .orderBy(Expressions.asNumber(projectTechTag.id.count()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Project> projects = result.stream().map(r -> r.get(project)).collect(Collectors.toList());
        return projects.stream().map(ProjectDto::new).collect(Collectors.toList());
    }
}
