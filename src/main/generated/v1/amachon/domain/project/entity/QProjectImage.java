package v1.amachon.domain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProjectImage is a Querydsl query type for ProjectImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectImage extends EntityPathBase<ProjectImage> {

    private static final long serialVersionUID = -942920522L;

    public static final QProjectImage projectImage = new QProjectImage("projectImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public QProjectImage(String variable) {
        super(ProjectImage.class, forVariable(variable));
    }

    public QProjectImage(Path<? extends ProjectImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProjectImage(PathMetadata metadata) {
        super(ProjectImage.class, metadata);
    }

}

