package v1.amachon.domain.tags.entity.techtag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTechTag is a Querydsl query type for TechTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTechTag extends EntityPathBase<TechTag> {

    private static final long serialVersionUID = -630909758L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTechTag techTag = new QTechTag("techTag");

    public final ListPath<TechTag, QTechTag> children = this.<TechTag, QTechTag>createList("children", TechTag.class, QTechTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QTechTag parent;

    public QTechTag(String variable) {
        this(TechTag.class, forVariable(variable), INITS);
    }

    public QTechTag(Path<? extends TechTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTechTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTechTag(PathMetadata metadata, PathInits inits) {
        this(TechTag.class, metadata, inits);
    }

    public QTechTag(Class<? extends TechTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QTechTag(forProperty("parent"), inits.get("parent")) : null;
    }

}

