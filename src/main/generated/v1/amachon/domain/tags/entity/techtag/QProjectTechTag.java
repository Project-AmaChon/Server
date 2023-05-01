package v1.amachon.domain.tags.entity.techtag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectTechTag is a Querydsl query type for ProjectTechTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectTechTag extends EntityPathBase<ProjectTechTag> {

    private static final long serialVersionUID = -1160841619L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectTechTag projectTechTag = new QProjectTechTag("projectTechTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final v1.amachon.domain.project.entity.QProject project;

    public final QTechTag techTag;

    public QProjectTechTag(String variable) {
        this(ProjectTechTag.class, forVariable(variable), INITS);
    }

    public QProjectTechTag(Path<? extends ProjectTechTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectTechTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectTechTag(PathMetadata metadata, PathInits inits) {
        this(ProjectTechTag.class, metadata, inits);
    }

    public QProjectTechTag(Class<? extends ProjectTechTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new v1.amachon.domain.project.entity.QProject(forProperty("project"), inits.get("project")) : null;
        this.techTag = inits.isInitialized("techTag") ? new QTechTag(forProperty("techTag"), inits.get("techTag")) : null;
    }

}

