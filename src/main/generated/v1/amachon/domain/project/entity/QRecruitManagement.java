package v1.amachon.domain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruitManagement is a Querydsl query type for RecruitManagement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruitManagement extends EntityPathBase<RecruitManagement> {

    private static final long serialVersionUID = -1886197843L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruitManagement recruitManagement = new QRecruitManagement("recruitManagement");

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

    public QRecruitManagement(String variable) {
        this(RecruitManagement.class, forVariable(variable), INITS);
    }

    public QRecruitManagement(Path<? extends RecruitManagement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruitManagement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruitManagement(PathMetadata metadata, PathInits inits) {
        this(RecruitManagement.class, metadata, inits);
    }

    public QRecruitManagement(Class<? extends RecruitManagement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new v1.amachon.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

