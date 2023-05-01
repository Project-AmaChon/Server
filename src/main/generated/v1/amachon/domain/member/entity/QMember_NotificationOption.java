package v1.amachon.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember_NotificationOption is a Querydsl query type for NotificationOption
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMember_NotificationOption extends EnumPath<Member.NotificationOption> {

    private static final long serialVersionUID = -139052133L;

    public static final QMember_NotificationOption notificationOption = new QMember_NotificationOption("notificationOption");

    public QMember_NotificationOption(String variable) {
        super(Member.NotificationOption.class, forVariable(variable));
    }

    public QMember_NotificationOption(Path<Member.NotificationOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember_NotificationOption(PathMetadata metadata) {
        super(Member.NotificationOption.class, metadata);
    }

}

