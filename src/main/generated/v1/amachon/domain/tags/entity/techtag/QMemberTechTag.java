package v1.amachon.domain.tags.entity.techtag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberTechTag is a Querydsl query type for MemberTechTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberTechTag extends EntityPathBase<MemberTechTag> {

    private static final long serialVersionUID = -490327480L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberTechTag memberTechTag = new QMemberTechTag("memberTechTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final v1.amachon.domain.member.entity.QMember member;

    public final QTechTag techTag;

    public QMemberTechTag(String variable) {
        this(MemberTechTag.class, forVariable(variable), INITS);
    }

    public QMemberTechTag(Path<? extends MemberTechTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberTechTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberTechTag(PathMetadata metadata, PathInits inits) {
        this(MemberTechTag.class, metadata, inits);
    }

    public QMemberTechTag(Class<? extends MemberTechTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new v1.amachon.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.techTag = inits.isInitialized("techTag") ? new QTechTag(forProperty("techTag"), inits.get("techTag")) : null;
    }

}

