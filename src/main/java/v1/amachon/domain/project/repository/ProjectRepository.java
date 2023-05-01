package v1.amachon.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.project.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p, COUNT(t.id) AS tag_count " +
            "FROM Project p\n" +
            "INNER JOIN ProjectTechTag pt ON p.id = pt.project.id\n" +
            "INNER JOIN TechTag t ON pt.techTag.id = t.id\n" +
            "WHERE t.name IN (:tagNames)\n" +
            "  AND p.regionTag.id = :regionId\n" +
            "GROUP BY p.id\n" +
            "ORDER BY tag_count DESC"
    )
    List<Project> searchProjectByTags(@Param("tagNames") List<String> tagNames, @Param("regionId")Long regionId);
}
