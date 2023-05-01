package v1.amachon.domain.tags.entity.regiontag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegionTag is a Querydsl query type for RegionTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegionTag extends EntityPathBase<RegionTag> {

    private static final long serialVersionUID = -1514723706L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegionTag regionTag = new QRegionTag("regionTag");

    public final ListPath<RegionTag, QRegionTag> children = this.<RegionTag, QRegionTag>createList("children", RegionTag.class, QRegionTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QRegionTag parent;

    public QRegionTag(String variable) {
        this(RegionTag.class, forVariable(variable), INITS);
    }

    public QRegionTag(Path<? extends RegionTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegionTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegionTag(PathMetadata metadata, PathInits inits) {
        this(RegionTag.class, metadata, inits);
    }

    public QRegionTag(Class<? extends RegionTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QRegionTag(forProperty("parent"), inits.get("parent")) : null;
    }

}

