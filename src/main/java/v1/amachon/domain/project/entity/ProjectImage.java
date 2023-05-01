package v1.amachon.domain.project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage {

    @Id
    @GeneratedValue
    @Column(name = "project_image_id")
    private Long id;
    private String imageUrl;

    public ProjectImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
