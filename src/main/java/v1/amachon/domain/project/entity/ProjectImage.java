package v1.amachon.domain.project.entity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "project_image_id")
    private Long id;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // 추가: 생성자를 통해 Project 엔티티를 매개변수로 받아 설정
    public ProjectImage(String imageUrl, Project project) {
        this.imageUrl = imageUrl;
        this.project = project;
        project.addImage(this);
    }
}
