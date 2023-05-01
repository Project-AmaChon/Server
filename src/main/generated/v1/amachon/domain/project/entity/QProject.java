package v1.amachon.domain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -2121659483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final v1.amachon.domain.base.QBaseEntity _super = new v1.amachon.domain.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> developPeriod = createDate("developPeriod", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ProjectImage, QProjectImage> images = this.<ProjectImage, QProjectImage>createList("images", ProjectImage.class, QProjectImage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final v1.amachon.domain.member.entity.QMember leader;

    public final DatePath<java.time.LocalDate> recruitDeadline = createDate("recruitDeadline", java.time.LocalDate.class);

    public final ListPath<RecruitManagement, QRecruitManagement> recruitManagements = this.<RecruitManagement, QRecruitManagement>createList("recruitManagements", RecruitManagement.class, QRecruitManagement.class, PathInits.DIRECT2);

    public final NumberPath<Integer> recruitNumber = createNumber("recruitNumber", Integer.class);

    public final v1.amachon.domain.tags.entity.regiontag.QRegionTag regionTag;

    //inherited
    public final EnumPath<v1.amachon.domain.base.BaseEntity.Status> status = _super.status;

    public final ListPath<TeamMember, QTeamMember> teamMembers = this.<TeamMember, QTeamMember>createList("teamMembers", TeamMember.class, QTeamMember.class, PathInits.DIRECT2);

    public final ListPath<v1.amachon.domain.tags.entity.techtag.ProjectTechTag, v1.amachon.domain.tags.entity.techtag.QProjectTechTag> techTags = this.<v1.amachon.domain.tags.entity.techtag.ProjectTechTag, v1.amachon.domain.tags.entity.techtag.QProjectTechTag>createList("techTags", v1.amachon.domain.tags.entity.techtag.ProjectTechTag.class, v1.amachon.domain.tags.entity.techtag.QProjectTechTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProject(PathMetadata metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.leader = inits.isInitialized("leader") ? new v1.amachon.domain.member.entity.QMember(forProperty("leader"), inits.get("leader")) : null;
        this.regionTag = inits.isInitialized("regionTag") ? new v1.amachon.domain.tags.entity.regiontag.QRegionTag(forProperty("regionTag"), inits.get("regionTag")) : null;
    }

}

