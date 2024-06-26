package v1.amachon.tags.entity.techtag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.common.entity.BaseEntity;
import v1.amachon.project.entity.Project;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectTechTag extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "project_tech_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_tag")
    private TechTag techTag;

    public ProjectTechTag(Project project, TechTag techTag) {
        this.project = project;
        this.techTag = techTag;
    }
}
