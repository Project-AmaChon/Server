package v1.amachon.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.project.entity.ProjectImage;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
}
