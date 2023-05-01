package v1.amachon.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1936173971L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final v1.amachon.domain.base.QBaseEntity _super = new v1.amachon.domain.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<v1.amachon.domain.message.entity.MessageRoom, v1.amachon.domain.message.entity.QMessageRoom> messageRooms = this.<v1.amachon.domain.message.entity.MessageRoom, v1.amachon.domain.message.entity.QMessageRoom>createList("messageRooms", v1.amachon.domain.message.entity.MessageRoom.class, v1.amachon.domain.message.entity.QMessageRoom.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final EnumPath<Member.NotificationOption> notificationOption = createEnum("notificationOption", Member.NotificationOption.class);

    public final ListPath<v1.amachon.domain.notification.entity.Notification, v1.amachon.domain.notification.entity.QNotification> notifications = this.<v1.amachon.domain.notification.entity.Notification, v1.amachon.domain.notification.entity.QNotification>createList("notifications", v1.amachon.domain.notification.entity.Notification.class, v1.amachon.domain.notification.entity.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final QProfile profile;

    public final v1.amachon.domain.tags.entity.regiontag.QRegionTag regionTag;

    //inherited
    public final EnumPath<v1.amachon.domain.base.BaseEntity.Status> status = _super.status;

    public final ListPath<v1.amachon.domain.tags.entity.techtag.MemberTechTag, v1.amachon.domain.tags.entity.techtag.QMemberTechTag> techTags = this.<v1.amachon.domain.tags.entity.techtag.MemberTechTag, v1.amachon.domain.tags.entity.techtag.QMemberTechTag>createList("techTags", v1.amachon.domain.tags.entity.techtag.MemberTechTag.class, v1.amachon.domain.tags.entity.techtag.QMemberTechTag.class, PathInits.DIRECT2);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new QProfile(forProperty("profile")) : null;
        this.regionTag = inits.isInitialized("regionTag") ? new v1.amachon.domain.tags.entity.regiontag.QRegionTag(forProperty("regionTag"), inits.get("regionTag")) : null;
    }

}

