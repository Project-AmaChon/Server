package v1.amachon.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.tags.entity.techtag.ProjectTechTag;

public interface ProjectTechTagRepository extends JpaRepository<ProjectTechTag, Long> {
}
