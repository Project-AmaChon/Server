package v1.amachon.domain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeamMember is a Querydsl query type for TeamMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeamMember extends EntityPathBase<TeamMember> {

    private static final long serialVersionUID = -1330273461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeamMember teamMember = new QTeamMember("teamMember");

    public final v1.amachon.domain.base.QBaseEntity _super = new v1.amachon.domain.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final v1.amachon.domain.member.entity.QMember member;

    public final QProject project;

    //inherited
    public final EnumPath<v1.amachon.domain.base.BaseEntity.Status> status = _super.status;

    public QTeamMember(String variable) {
        this(TeamMember.class, forVariable(variable), INITS);
    }

    public QTeamMember(Path<? extends TeamMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeamMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeamMember(PathMetadata metadata, PathInits inits) {
        this(TeamMember.class, metadata, inits);
    }

    public QTeamMember(Class<? extends TeamMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new v1.amachon.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

