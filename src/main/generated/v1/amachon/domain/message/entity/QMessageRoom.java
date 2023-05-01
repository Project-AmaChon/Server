package v1.amachon.domain.message.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageRoom is a Querydsl query type for MessageRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageRoom extends EntityPathBase<MessageRoom> {

    private static final long serialVersionUID = -1436988320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageRoom messageRoom = new QMessageRoom("messageRoom");

    public final v1.amachon.domain.base.QBaseEntity _super = new v1.amachon.domain.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final v1.amachon.domain.member.entity.QMember from;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<Message, QMessage> messages = this.<Message, QMessage>createList("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    public final BooleanPath readCheck = createBoolean("readCheck");

    //inherited
    public final EnumPath<v1.amachon.domain.base.BaseEntity.Status> status = _super.status;

    public final v1.amachon.domain.member.entity.QMember to;

    public final QMessageRoom toSend;

    public QMessageRoom(String variable) {
        this(MessageRoom.class, forVariable(variable), INITS);
    }

    public QMessageRoom(Path<? extends MessageRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageRoom(PathMetadata metadata, PathInits inits) {
        this(MessageRoom.class, metadata, inits);
    }

    public QMessageRoom(Class<? extends MessageRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new v1.amachon.domain.member.entity.QMember(forProperty("from"), inits.get("from")) : null;
        this.to = inits.isInitialized("to") ? new v1.amachon.domain.member.entity.QMember(forProperty("to"), inits.get("to")) : null;
        this.toSend = inits.isInitialized("toSend") ? new QMessageRoom(forProperty("toSend"), inits.get("toSend")) : null;
    }

}

